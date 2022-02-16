package com.miniproject2.bookcafe.service;

import com.miniproject2.bookcafe.domain.Moim;
import com.miniproject2.bookcafe.domain.MoimMember;
import com.miniproject2.bookcafe.domain.User;
import com.miniproject2.bookcafe.dto.CommentResponseDto;
import com.miniproject2.bookcafe.dto.MoimRequestDto;
import com.miniproject2.bookcafe.dto.MoimResponseDto;
import com.miniproject2.bookcafe.dto.UserRequestDto;
import com.miniproject2.bookcafe.repository.MoimMemberRepository;
import com.miniproject2.bookcafe.repository.MoimRepository;
import com.miniproject2.bookcafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoimService {

    private final MoimRepository moimRepository;
    private final UserRepository userRepository;

    @Autowired
    public MoimService(MoimRepository moimRepository,
                       UserRepository userRepository){
        this.moimRepository = moimRepository;
        this.userRepository = userRepository;
    }

    public Moim createMoim(Moim moim){
        moimRepository.save(moim);
        return moim;
    }

    public List<MoimResponseDto> getMoims(){
        // 최신 순으로 모임 리스트 정렬
        List<Moim> moimList= moimRepository.findAllByOrderByCreatedAtDesc();
        // 응답을 위한 리스트 생성
        List<MoimResponseDto> moimResponseDtos = new ArrayList<>();

        // 모임 멤버의 닉네임을 리스트에 추가함
        for(Moim moim : moimList) {
            List<MoimMember> moimMemberList = moim.getMoimMembers();
            if (moimMemberList != null) {
                List<String> joinMembers = new ArrayList<>();
                for (MoimMember moimMember : moimMemberList) {
                    joinMembers.add(moimMember.getUser().getNickname());
                }
                MoimResponseDto moimResponseDto =
                        new MoimResponseDto(moim, joinMembers);
                moimResponseDtos.add(moimResponseDto);
            }
        }
        return moimResponseDtos;
    }


    @Transactional
    public Long update(Long moimId, MoimRequestDto requestDto){
        Moim moim =  moimRepository.findById(moimId).orElseThrow(
                () -> new IllegalArgumentException("모임이 존재하지 않습니다.")
        );
        moim.update(requestDto);
        return moimId;
    }



    public MoimResponseDto getMoimDetails(Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(
                () -> new IllegalArgumentException("모임이 존재하지 않습니다.")
        );

        List<MoimMember> moimMemberList = moim.getMoimMembers();
        List<String> joinMembers = new ArrayList<>();
        for(MoimMember moimMember : moimMemberList){
            joinMembers.add(moimMember.getUser().getNickname());
        }
        return new MoimResponseDto(moim, joinMembers);
    }


    public List<MoimResponseDto> getUserMoims(@RequestBody UserRequestDto requestDto){
        User user = userRepository.findByNickname(requestDto.getNickname());
        List<MoimMember> moimMembers = user.getMoimMembers();

        List<MoimResponseDto> moimResponseDtos = new ArrayList<>();
        for (MoimMember moimMember : moimMembers){
            Moim moim = moimMember.getMoim();
            MoimResponseDto responseDto = new MoimResponseDto(moim);
            moimResponseDtos.add(responseDto);
        }
        return moimResponseDtos.stream().
                sorted(Comparator.comparing(MoimResponseDto::getCreatedAt).reversed()).
                collect(Collectors.toList());
    }


}
