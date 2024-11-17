//package com.project.common.listener;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//
//@Component
//public class DatabaseConnection implements ApplicationListener<ApplicationReadyEvent> {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        try  {
//            dataSource.getConnection();
//            System.out.println("資料庫連線成功！");
//        } catch (Exception e) {
//            System.out.println("資料庫連線失敗：" + e.getMessage());
//        }
//    }
//}
//
