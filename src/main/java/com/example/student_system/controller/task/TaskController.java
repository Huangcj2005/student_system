package com.example.student_system.controller.task;

import com.example.student_system.annotation.LogAction;
import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.account.UserInfo;
import com.example.student_system.domain.dto.task.ExamDTO;
import com.example.student_system.domain.dto.task.HomeworkDTO;
import com.example.student_system.domain.dto.task.QuestionRecordDTO;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.task.Exam;
import com.example.student_system.domain.entity.task.Homework;
import com.example.student_system.domain.entity.task.QuestionRecord;
import com.example.student_system.domain.vo.course.CourseVo;
import com.example.student_system.domain.vo.task.*;
import com.example.student_system.service.account.UserInfoService;
import com.example.student_system.service.course.EnrollmentService;
import com.example.student_system.service.task.ExamService;
import com.example.student_system.service.task.HomeworkService;
import com.example.student_system.service.task.ScoreService;
import com.example.student_system.util.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/task")
public class TaskController
{

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ExamService examService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ScoreService scoreService;

    private static final String uploadRoot = "D:/uploads/";

    // 查看个人所有作业url
    @LogAction("用户查看个人所有课程")
    @RequestMapping(value = "/homework", method = RequestMethod.GET)
    public CommonResponse<List<HomeworkVO>> getHomework()
    {
        List<HomeworkVO> homeworkVOList = new ArrayList<>();
        List<Homework> homeworkList = homeworkService.getHomeworkByUserid(UserContext.getCurrentUserId()).getData();
        for(Homework homework : homeworkList)
        {
            HomeworkVO homeworkVO = new HomeworkVO();
            BeanUtils.copyProperties(homework, homeworkVO);
            homeworkVOList.add(homeworkVO);
        }
        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getDescription(),
                homeworkVOList
        );
    }

    // 查看个人选的某门课的全部作业
    @LogAction("用户查看课程全部作业")
    @RequestMapping(value = "/homework/{course_id}", method = RequestMethod.GET)
    public CommonResponse<List<HomeworkVO>> getCourseHomework(
            @PathVariable int course_id
    )
    {
        List<HomeworkVO> homeworkVOList = new ArrayList<>();
        List<Homework> homeworkList = homeworkService.getHomeworkByUserid(UserContext.getCurrentUserId(), course_id).getData();
        for(Homework homework : homeworkList)
        {
            HomeworkVO homeworkVO = new HomeworkVO();
            BeanUtils.copyProperties(homework, homeworkVO);
            homeworkVOList.add(homeworkVO);
        }
        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_LIST_FETCH_SUCCESS.getDescription(),
                homeworkVOList
        );
    }

    // 查看作业详情
    @LogAction("查看作业详情")
    @RequestMapping(value = "/homework/{course_id}/detail", method = RequestMethod.GET)
    public CommonResponse<HomeworkVO> getHomeworkDetail(
            @PathVariable int course_id,
            @RequestParam("title") String title
            )
    {
        return homeworkService.getHomeworkDetail(UserContext.getCurrentUserId() ,course_id, title);
    }


    // 发布作业url，需要前段将作业基本信息放在assignment字段中，文件放在files当中
    @LogAction("教师发布作业")
    @PostMapping(value = "/homework/{course_id}/assign")
    public CommonResponse<Homework> assignHomework(
            @PathVariable int course_id,
            @RequestPart("homework") String homeworkJson,
            @RequestPart(value = "files", required = false)MultipartFile[] files
    )
    {

        System.out.println(homeworkJson);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        HomeworkDTO homeworkDTO;
        try {
            homeworkDTO = objectMapper.readValue(homeworkJson, HomeworkDTO.class);
        } catch (IOException e) {
            return CommonResponse.createForError(
                    ResponseCode.HOMEWORK_JSON_ERROR.getCode(),
                    ResponseCode.HOMEWORK_JSON_ERROR.getDescription()
            );
        }
        // 设置允许的文件格式
        List<String> allowedTypes = Arrays.asList(".pdf", ".docx", ".zip", ".rar", ".xlsx");

        // 保存路径和URL前缀
        String uploadDir = uploadRoot + "assignments/" + course_id + "/";
        String urlPrefix = "http://localhost:8080/files/assignments/" + course_id + "/";

        // 创建保存目录（如不存在）
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();

        List<String> savedUrls = new ArrayList<>();
        // 获取对应课程下的所有用户
        List<User> userList = enrollmentService.getUsersByCourseId(course_id).getData();
        System.out.println(222);
        // 没有学生选则直接报错
        if (userList.isEmpty())
        {
            System.out.println(333);
            return CommonResponse.createForError(
                    ResponseCode.HOMEWORK_ASSIGN_FAIL.getCode(),
                    ResponseCode.HOMEWORK_ASSIGN_FAIL.getDescription()
            );
        }
        System.out.println(userList);

        // 先对标题可行性判断
        for(User user : userList)
        {
            if(homeworkService.isHomeworkExists(homeworkDTO.getHomework_title(), course_id, user.getUserId()))
                return CommonResponse.createForError(
                        ResponseCode.HOMEWORK_EXISTS.getCode(),
                        ResponseCode.HOMEWORK_EXISTS.getDescription()
                        );
        }

        if (files != null)
        {
            for (MultipartFile file : files)
            {
                String originalFilename = file.getOriginalFilename();
                // 获取后缀名
                String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

                // 类型校验
                if ( !allowedTypes.contains(suffix) )
                {
                    return CommonResponse.createForError(
                            ResponseCode.SUFFIX_NOT_ALLOWED.getCode(),
                            ResponseCode.SUFFIX_NOT_ALLOWED.getDescription()
                    );
                }

                // 为文件设置随机文件名以防止重名
                String newFileName = UUID.randomUUID() + suffix;
                File targetFile = new File(uploadDir + newFileName);
                try
                {
                    // 将文件存入本地
                    file.transferTo(targetFile);
                    // 记录需要在数据库中保存的url
                    savedUrls.add(urlPrefix + newFileName);
                }
                catch (IOException e)
                {
                    return CommonResponse.createForError(
                            ResponseCode.UPLOAD_FAILED.getCode(),
                            ResponseCode.UPLOAD_FAILED.getDescription()
                    );
                }
            }
        }


        //循环对所有学生发布
        for(User user : userList)
        {
            Homework homework = new Homework();
            BeanUtils.copyProperties(homeworkDTO, homework);
            homework.setCourse_id(course_id);
            homework.setAttachment_url(String.join(",", savedUrls));
            homework.setTeacher_id(UserContext.getCurrentUserId());
            homework.setUser_id(user.getUserId());
            homework.setStatus("0");
            homeworkService.assignHomework(homework);
        }

        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_ASSIGN_SUCCESS.getCode(),
                ResponseCode.HOMEWORK_ASSIGN_SUCCESS.getDescription()
        );
    }

    // 提交作业url
    @LogAction("用户提交作业")
    @RequestMapping(value = "/homework/{course_id}/submit", method = RequestMethod.POST)
    public CommonResponse<Homework> submitHomework(
            @PathVariable int course_id,
            @RequestPart("homework") String homeworkJson,
            @RequestPart(value = "files", required = false)MultipartFile[] files
    )
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        HomeworkDTO homeworkDTO;
        try {
            homeworkDTO = objectMapper.readValue(homeworkJson, HomeworkDTO.class);
        } catch (IOException e) {
            return CommonResponse.createForError(
                    ResponseCode.HOMEWORK_JSON_ERROR.getCode(),
                    ResponseCode.HOMEWORK_JSON_ERROR.getDescription()
            );
        }

        // 设置允许的文件格式
        List<String> allowedTypes = Arrays.asList(".pdf", ".docx", ".zip", ".rar", ".xlsx");

        // 保存路径和URL前缀
        String uploadDir = uploadRoot + "submissions/" + UserContext.getCurrentUserId() + "/";
        String urlPrefix = "http://localhost:8080/files/submissions/" + UserContext.getCurrentUserId() + "/";

        // 创建保存目录（如不存在）
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();

        List<String> savedUrls = new ArrayList<>();

        if (files != null)
        {
            for (MultipartFile file : files)
            {
                String originalFilename = file.getOriginalFilename();
                // 获取后缀名
                String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

                // 类型校验
                if (!allowedTypes.contains(suffix))
                {
                    return CommonResponse.createForError(
                            ResponseCode.SUFFIX_NOT_ALLOWED.getCode(),
                            ResponseCode.SUFFIX_NOT_ALLOWED.getDescription()
                    );
                }

                // 为文件设置随机文件名以防止重名
                String newFileName = UUID.randomUUID() + suffix;
                File targetFile = new File(uploadDir + newFileName);
                try
                {
                    // 将文件存入本地
                    file.transferTo(targetFile);
                    // 记录需要在数据库中保存的url
                    savedUrls.add(urlPrefix + newFileName);
                } catch (IOException e)
                {
                    return CommonResponse.createForError(
                            ResponseCode.UPLOAD_FAILED.getCode(),
                            ResponseCode.UPLOAD_FAILED.getDescription()
                    );
                }
            }
        }

        Homework homework = new Homework();
        BeanUtils.copyProperties(homeworkDTO, homework);
        homework.setSubmit_url(String.join(",", savedUrls));
        homework.setUser_id(UserContext.getCurrentUserId());
        homework.setCourse_id(course_id);
        return homeworkService.submitHomework(homework);
    }

    //FIXME:此处暴露了user_id,后续需改进设计
    @RequestMapping(value = "/homework/{course_id}/unremarked", method = RequestMethod.GET)
    public CommonResponse<List<Homework>> getUnremarkedHomework(@PathVariable int course_id)
    {
        // 查验教师身份
        Integer user_id = UserContext.getCurrentUserId();
        if(!userInfoService.getUserInfo(user_id).getData().getRole().equals("teacher"))
        {
            return CommonResponse.createForError(
                    ResponseCode.ERROR.getCode(),
                    ResponseCode.ERROR.getDescription()
            );
        }

        return homeworkService.getUnremarkedHomework(course_id);
    }

    // 评价作业url  必需: 课程id,学生id,标题,内容,评价,成绩
    @LogAction("教师评价作业")
    @RequestMapping(value = "/homework/{course_id}/remark", method = RequestMethod.POST)
    public CommonResponse<Homework> remarkHomework(
            @PathVariable int course_id,
            @RequestBody HomeworkDTO homeworkDTO
    )
    {
        Homework homework = new Homework();
        BeanUtils.copyProperties(homeworkDTO, homework);
        homework.setCourse_id(course_id);
        homework.setTeacher_id(UserContext.getCurrentUserId());
        return homeworkService.remarkHomework(homework);
    }

//    // 获取作业成绩url
//    @RequestMapping(value = "/homework/{user_id}/{course_id}/final_score", method = RequestMethod.GET)
//    public CommonResponse<BigDecimal> getHomeworkFinalScore(
//            @PathVariable int user_id,
//            @PathVariable int course_id
//    )
//    {
//        return homeworkService.getHomeworkScore(user_id, course_id);
//    }

    /*------------------------------------------------------------------------*/

    // 获取考试列表url
    @LogAction("用户获取课程考试列表")
    @RequestMapping(value = "/exam/{course_id}", method = RequestMethod.GET)
    public CommonResponse<List<ExamVO>> getExamList(@PathVariable int course_id)
    {
        //设置VO
        List<ExamVO> examVOList = new ArrayList<>();

        List<Exam> examList = examService.getExamList(course_id).getData();
        for(Exam exam : examList)
        {
            ExamVO examVO = new ExamVO();
            BeanUtils.copyProperties(exam, examVO);
            examVOList.add(examVO);
        }
        return CommonResponse.createForSuccess(
                ResponseCode.EXAM_LIST_FETCH_SUCCESS.getCode(),
                ResponseCode.EXAM_LIST_FETCH_SUCCESS.getDescription(),
                examVOList
        );
    }

    // 获取用户自己的考试列表
    @RequestMapping(value = "/exam/my_exam", method = RequestMethod.GET)
    public CommonResponse<List<ExamVO>> getStudentExamList()
    {
        Integer user_id = UserContext.getCurrentUserId();
        List<CourseVo> courseVoList = enrollmentService.getCoursesByUserId(user_id).getData();
        List<Integer> course_id_list = new ArrayList<>();
        for(CourseVo courseVo : courseVoList)
        {
            course_id_list.add(courseVo.getCourse_id());
        }

        return examService.getUserExamList(course_id_list);
    }

    @LogAction("用户获取已完成的考试")
    @RequestMapping(value = "/exam/finished_exam", method = RequestMethod.GET)
    public CommonResponse<List<ExamScoreVO>> getFinishedExam()
    {
        return examService.getFinishedExam(UserContext.getCurrentUserId());
    }

    // 教师创建考试
    @RequestMapping(value = "/exam/{course_id}/create", method = RequestMethod.POST)
    public CommonResponse<Exam> createExam(
            @PathVariable int course_id,
            @RequestBody ExamDTO examDTO
            )
    {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examDTO, exam);
        Integer user_id = UserContext.getCurrentUserId();
        exam.setTeacher_id(user_id);
        exam.setCourse_id(course_id);
        return examService.createExam(exam);
    }

    // 获取考试试卷
    @RequestMapping(value = "/exam/{exam_id}/paper", method = RequestMethod.GET)
    public CommonResponse<PaperVO> getExamPaper(@PathVariable int exam_id)
    {
        return examService.getPaper(exam_id);
    }

    /**
     DTO需要课程编号,答题的序号(question_id),类型,以及选择的答案
     */
    @LogAction("用户答题")
    @RequestMapping(value = "/exam/{exam_id}/student_answer", method = RequestMethod.POST)
    public CommonResponse<QuestionRecord> saveOrUpdateRecode(
            @PathVariable int exam_id,
            @RequestBody QuestionRecordDTO questionRecordDTO
            )
    {
        // 设置一个符合要求的问题记录
        QuestionRecord questionRecord = new QuestionRecord();

        BeanUtils.copyProperties(questionRecordDTO, questionRecord);
        questionRecord.setUser_id(UserContext.getCurrentUserId());
        questionRecord.setExam_id(exam_id);

        return examService.saveOrUpdateQuestionRecord(questionRecord);
    }

    @LogAction("用户提交试卷")
    @RequestMapping(value = "/exam/{exam_id}/submit", method = RequestMethod.POST)
    public CommonResponse<BigDecimal> submitExam(@PathVariable int exam_id)
    {
        Integer user_id = UserContext.getCurrentUserId();
        return examService.submitExam(user_id, exam_id);
    }

    @LogAction("教师发布试卷")
    @RequestMapping(value = "/exam/{exam_id}/generate_and_release", method = RequestMethod.GET)
    public CommonResponse<List<QuestionVO>> generateAndReleaseExam(
            @PathVariable int exam_id,
            @RequestParam String tags
    )
    {
        return examService.generatePaper(exam_id, 10, 10, 10, tags, true);
    }

    /*----------------------------------------------------*/

    // 计算并获取课程成绩
    @RequestMapping(value = "/score/{course_id}", method = RequestMethod.GET)
    public CommonResponse<ScoreVO> getCourseScore(@PathVariable int course_id)
    {
        return scoreService.updateAndGetScore(UserContext.getCurrentUserId(), course_id);
    }

}
