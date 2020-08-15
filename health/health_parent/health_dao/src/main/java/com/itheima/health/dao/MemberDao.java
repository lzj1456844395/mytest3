package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    List<Member> findAll();
    Page<Member> selectByCondition(String queryString);
    void add(Member member);
    void deleteById(Integer id);
    Member findById(Integer id);
    Member findByTelephone(String telephone);
    void edit(Member member);
    Integer findMemberCountBeforeDate(String date);
    Integer findMemberCountByDate(String date);
    Integer findMemberCountAfterDate(String date);
    Integer findMemberTotalCount();

    Integer findMemberCountBeforeDate1(String s);

    List<Map<String, Object>> getSexPart();

    Integer findCountByAge(@Param("begin") String begin,@Param("end") String end);
}
