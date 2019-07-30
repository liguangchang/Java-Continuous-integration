package com.jasmine;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * optional
 *
 * @author guangchang
 * @create 2019-07-30 21:31
 **/

public class OptionalDemo {
    public static void main(String[] args) {
        Optional<Object> empty = Optional.empty();
        // empty.get(); nullPointException
        Optional<String> s = Optional.of("");
        System.err.println(s);
        //  Optional<Optional<Object>> opt = Optional.of(null);
        // System.err.println(opt);
        Optional<Object> opt1 = Optional.ofNullable(null);
        System.err.println(opt1);
        /**
         * 创建optional of/ofNullable
         * optional.of()会产生nullPointException
         * 取optional对象中的值
         *  get()
         *  isPresent() 检查是否有值 接受一个consumer参数
         *  返回对象值
         *  orElse()
         *
         */




    }
    @Test
    public void test(){
        Optional<String> name = Optional.ofNullable("name");
        String s1 = name.get();
        System.err.println(s1);
        boolean present = name.isPresent();
        assertTrue(name.isPresent());
        assertEquals("name",name.get());
        Optional<String> s = Optional.ofNullable(name).orElse(null);
        System.err.println(s);

    }
}
