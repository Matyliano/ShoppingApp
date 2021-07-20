package com.example.shopping.controller.history;


import com.example.shopping.dto.UserDto;
import com.example.shopping.mapper.history.UserHistoryMapper;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history/users")
@RequiredArgsConstructor
public class UserHistoryController {
    private final UserRepository userRepository;
    private final UserHistoryMapper userHistoryMapper;

    @GetMapping("/{id}")
    public Page<UserDto> getUserHistory(@RequestParam int page, @RequestParam int size, @PathVariable Long id){
        return userRepository.findRevisions(id, PageRequest.of(page, size)).map(userHistoryMapper::toDto);
    }
}
