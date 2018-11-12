package com.jaagro.tms.web.controller;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeTest {

    public static List<Employee> generateData() {
        return Arrays.asList(new Employee("Matt", 5000, "New York"),
                new Employee("Steve", 6000, "London"),
                new Employee("Carrie", 10000, "New York"),
                new Employee("Peter", 7000, "New York"),
                new Employee("Alec", 6000, "London"),
                new Employee("Sarah", 8000, "London"),
                new Employee("Rebecca", 4000, "New York"),
                new Employee("Pat", 20000, "New York"),
                new Employee("Tammy", 9000, "New York"),
                new Employee("Fred", 15000, "Tokyo"));
    }

    public static Map<String, Integer> generateMapData() {
        Map<String, Integer> items = Maps.newHashMap();
        items.put("A", 10);
        items.put("B", 20);
        items.put("C", 30);
        items.put("D", 40);
        items.put("E", 50);
        items.put("F", 60);

        return items;
    }

    public static void main(String[] args) {
        List<Employee> employeeList = generateData();
        //返回姓名列表
        List<String> names = employeeList.stream().map(Employee::getName).collect(Collectors.toList());
        System.out.println(names);
    }
}


