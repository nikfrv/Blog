package com.web.blog.controller;

import com.web.blog.core.domain.dto.VoteDto;
import com.web.blog.core.service.impl.VoteServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {

    private final VoteServiceImpl voteServiceImpl;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteServiceImpl.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}