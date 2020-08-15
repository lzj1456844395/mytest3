package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 通过手机号码查询会员信息
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 添加会员
     * @param member
     */
    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 统计过去一年的会员总数
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        //select count(1) from t_member where regTime<='2020-06-31';  <= Before
        List<Integer> memberCount = new ArrayList<Integer>();
        if(months != null) {
            // 循环12个，每个月查询一下
            for (String month : months) {
                // 到这个month为，一会有多少个会员
                Integer count = memberDao.findMemberCountBeforeDate(month + "-31");
                memberCount.add(count);
            }
        }
        return memberCount;
    }
}
