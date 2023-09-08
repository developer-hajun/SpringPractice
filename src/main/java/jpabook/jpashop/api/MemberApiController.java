package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller,@ResponseBody
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    @PostMapping("/api/v1/member")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    } // -> 해당부분은 엔티티가 외부로 노출 되어 있어서 사용x

    @GetMapping("/api/v1/member")
    public List<Member> showMemberV1(){
        return memberService.findMembers();
    } //이렇게 엔티티 직접 노출x why?? -> 엔티티 변경 -> api스펙 변경
    //리스트로 반환 x why? -> json확장이 불가능해짐

    @PostMapping("/api/v2/member")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long join = memberService.join(member);
        return new CreateMemberResponse(join);
    }
    @GetMapping("/api/v2/member")
    public Result showMemberV2 (){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    // 번역하기

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String name;
    }
    @PutMapping("/api/v2/member/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());
        Member one = memberService.findOne(id);
        return new UpdateMemberResponse(one.getName(),one.getId());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private String name;
        private Long id;
    }
    @Data
    static class UpdateMemberRequest{
        private String name;
    }
    @Data
    static class CreateMemberRequest{
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;
        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }
}
