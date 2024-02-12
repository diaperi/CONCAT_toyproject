package test.toyProject.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.toyProject.user.dto.UserDTO;
import test.toyProject.user.entity.UserEntity;
import test.toyProject.user.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public void save(UserDTO userDTO) {
        // 1. dto -> entity 변환
        // 2. repository의 signup 메서드 호출
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        userRepository.save(userEntity);
        // repository의 signup 메서드 호출 (조건. entity 객체를 넘겨줘야 함)
    }

    public UserDTO login(UserDTO userDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<UserEntity> byEmail = userRepository.findByEmail(userDTO.getEmail());
        if(byEmail.isPresent()){
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            UserEntity userEntity = byEmail.get();
            if(userEntity.getPassword().equals(userDTO.getPassword())){
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                UserDTO dto = UserDTO.toUserDTO(userEntity);
                return dto;
            }else{
                // 비밀번호 불일치
                return null;
            }
        }else{
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public String emailCheck(String email) {
        Optional<UserEntity> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()){
            // 조회결과가 있다 -> 사용할 수 없다.
            return null;
        }else{
            // 조회결과가 없다 -> 사용할 수 있다.
            return "ok";
        }
    }


    // 댓글용
    public String getFullNameByEmail(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return user.getFirstName() + " " + user.getLastName();
        } else {
            return null;
        }
    }
}
