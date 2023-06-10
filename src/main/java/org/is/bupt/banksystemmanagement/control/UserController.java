package org.is.bupt.banksystemmanagement.control;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.is.bupt.banksystemmanagement.common.R;
import org.is.bupt.banksystemmanagement.entities.User;
import org.is.bupt.banksystemmanagement.service.UserService;
import org.is.bupt.banksystemmanagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;


@Slf4j
@RestController
//@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @Autowired
//    private HttpServletRequest httpServletRequest;


    public User findById(long userId){
//        var session = httpServletRequest.getSession();
//        var userId = session.getAttribute("userId");

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserId,userId);
        return userService.getOne(userLambdaQueryWrapper);
    }

//    @RequestMapping("find")
//    public int find(@RequestParam long userId) throws ParseException {
//        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        userLambdaQueryWrapper.eq(User::getUserId,userId);
//        var user = userService.getOne(userLambdaQueryWrapper);
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//        var res = dateFormatter.format(user.getBirth());
//        var birth = user.getBirth();
//        var now = new Date();
//        var ca = Calendar.getInstance();
//        ca.setTime(birth);
//        int year = ca.get(Calendar.YEAR)+7;
//
//
//        var v = dateFormatter.parse("1998-01-31");
//        return user.getBirth().compareTo(v);
//    }

//    public int getAge(Date birthDay) throws Exception {
//        // 获取当前系统时间
//        Calendar cal = Calendar.getInstance();
//        // 如果出生日期大于当前时间，则抛出异常
//        if (cal.before(birthDay)) {
//            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
//        }
//        // 取出系统当前时间的年、月、日部分
//        int yearNow = cal.get(Calendar.YEAR);
//        int monthNow = cal.get(Calendar.MONTH);
//        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
//
//        // 将日期设置为出生日期
//        cal.setTime(birthDay);
//        // 取出出生日期的年、月、日部分
//        int yearBirth = cal.get(Calendar.YEAR);
//        int monthBirth = cal.get(Calendar.MONTH);
//        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
//        // 当前年份与出生年份相减，初步计算年龄
//        int age = yearNow - yearBirth;
//        // 当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
//        if (monthNow <= monthBirth) {
//            // 如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
//            if (monthNow == monthBirth) {
//                if (dayOfMonthNow < dayOfMonthBirth)
//                    age--;
//            } else {
//                age--;
//            }
//        }
//        return age;
//    }
}
