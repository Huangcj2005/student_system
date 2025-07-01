package com.example.student_system.controller.task;

import com.example.student_system.common.CommonResponse;
import com.example.student_system.common.ResponseCode;
import com.example.student_system.domain.dto.task.HomeworkDTO;
import com.example.student_system.domain.entity.account.User;
import com.example.student_system.domain.entity.task.Exam;
import com.example.student_system.domain.entity.task.Homework;
import com.example.student_system.domain.vo.task.HomeworkVO;
import com.example.student_system.service.course.EnrollmentService;
import com.example.student_system.service.task.ExamService;
import com.example.student_system.service.task.HomeworkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/task")
public class TaskController
{

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ExamService examService;

    private static final String uploadRoot = "D:/uploads/";

    // 查看个人所有作业url
    @RequestMapping(value = "/homework/{user_id}", method = RequestMethod.GET)
    public CommonResponse<List<Homework>> getHomework(@PathVariable int user_id)
    {
        return homeworkService.getHomeworkByUserid(user_id);
    }

    // 查看个人选的某门课的全部作业
    @RequestMapping(value = "/homework/{user_id}/{course_id}", method = RequestMethod.GET)
    public CommonResponse<List<Homework>> getCourseHomework(
            @PathVariable int user_id,
            @PathVariable int course_id
    )
    {
        return homeworkService.getHomeworkByUserid(user_id, course_id);
    }

    // 查看作业详情
    @RequestMapping(value = "/homework/{course_id}/detail", method = RequestMethod.GET)
    public CommonResponse<HomeworkVO> getHomeworkDetail(
            @PathVariable int course_id,
            @RequestParam String title
            )
    {
        return homeworkService.getHomeworkDetail(course_id, title);
    }


    // 发布作业url，需要前段将作业基本信息放在assignment字段中，文件放在files当中
    @RequestMapping(value = "/homework/{teacher_id}/{course_id}/assign", method = RequestMethod.POST)
    public CommonResponse<Homework> assignHomework(
            @PathVariable int teacher_id,
            @PathVariable int course_id,
            @RequestPart("homework") HomeworkDTO homeworkDTO,
            @RequestPart(value = "files", required = false)MultipartFile[] files
    )
    {
        // 设置允许的文件格式
        List<String> allowedTypes = Arrays.asList(".pdf", ".docx", ".zip", ".rar", ".xlsx");

        // 保存路径和URL前缀
        String uploadDir = uploadRoot + "assignments/" + homeworkDTO.getCourse_id() + "/";
        String urlPrefix = "http://localhost:8080/files/assignments/" + homeworkDTO.getCourse_id() + "/";

        // 创建保存目录（如不存在）
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();

        List<String> savedUrls = new ArrayList<>();
        // 获取对应课程下的所有用户
        List<User> userList = enrollmentService.getUsersByCourseId(homeworkDTO.getCourse_id()).getData();

        // 先对标题可行性判断
        for(User user : userList)
        {
            if(homeworkService.isHomeworkExists(homeworkDTO.getHomework_title(), homeworkDTO.getCourse_id(), user.getUserId()))
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
            homework.setTeacher_id(teacher_id);
            homework.setUser_id(user.getUserId());
            homework.setStatus("0");
            homeworkService.assignHomework(homework);
        }
        return CommonResponse.createForSuccess(
                ResponseCode.HOMEWORK_ASSIGN_SUCCESS.getCode(),
                ResponseCode.USER_REGISTER_SUCCESS.getDescription()
        );
    }

    // 提交作业url
    @RequestMapping(value = "/homework/{user_id}/submit", method = RequestMethod.POST)
    public CommonResponse<Homework> submitHomework(
            @PathVariable int user_id,
            @RequestPart("homework") HomeworkDTO homeworkDTO,
            @RequestPart(value = "files", required = false)MultipartFile[] files
    )
    {
        // 设置允许的文件格式
        List<String> allowedTypes = Arrays.asList(".pdf", ".docx", ".zip", ".rar", ".xlsx");

        // 保存路径和URL前缀
        String uploadDir = uploadRoot + "submissions/" + user_id + "/";
        String urlPrefix = "http://localhost:8080/files/submissions/" + user_id + "/";

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
        homework.setUser_id(user_id);
        return homeworkService.submitHomework(homework);
    }

    // 评价作业url  必需: 课程id,学生id,标题,内容,评价,成绩
    @RequestMapping(value = "/homework/{teacher_id}/remark", method = RequestMethod.POST)
    public CommonResponse<Homework> remarkHomework(
            @PathVariable int teacher_id,
            @RequestPart("homework") HomeworkDTO homeworkDTO
    )
    {
        Homework homework = new Homework();
        BeanUtils.copyProperties(homeworkDTO, homework);
        return homeworkService.remarkHomework(homework);
    }

    // 获取作业成绩url
    @RequestMapping(value = "/homework/{user_id}/{course_id}/final_score", method = RequestMethod.GET)
    public CommonResponse<BigDecimal> getHomeworkFinalScore(
            @PathVariable int user_id,
            @PathVariable int course_id
    )
    {
        return homeworkService.getHomeworkScore(user_id, course_id);
    }


    /*------------------------------------------------------------------------*/

    // 获取考试列表url
    @RequestMapping(value = "/exam/{course_id}", method = RequestMethod.GET)
    public CommonResponse<List<Exam>> getExamList(@PathVariable int course_id)
    {
        return examService.getExamList(course_id);
    }
}
