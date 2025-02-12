package com.ssafy.trip.attraction.controller;

import com.ssafy.trip.attraction.model.dto.command.AttractionDescRefreshCommandDto;
import com.ssafy.trip.attraction.model.dto.command.MemberDistCommandDto;
import com.ssafy.trip.attraction.model.dto.request.NearestAttractionRequestDto;
import com.ssafy.trip.attraction.model.dto.request.PopularAttractionRequestDto;
import com.ssafy.trip.attraction.model.service.AttractionService;
import com.ssafy.trip.global.util.exception.MyException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attraction")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping("content-type/refresh")
    public ResponseEntity<?> refreshContentType() {
        return ResponseEntity.ok(attractionService.refreshContentType());
    }

    @GetMapping("cat/refresh")
    public ResponseEntity<?> refreshCat() {
        return ResponseEntity.ok(attractionService.refreshCat(1, "", ""));
    }

    @GetMapping("refresh")
    public ResponseEntity<?> refreshAttraction() {
        return ResponseEntity.ok(attractionService.refreshAttraction());
    }
    @GetMapping("{contentId}/refresh")
    public ResponseEntity<?> refreshAttractionDesc(@PathVariable("contentId") int contentId) {
        return ResponseEntity.ok(attractionService.refreshAttractionDesc(AttractionDescRefreshCommandDto.of(contentId)));
    }
    @GetMapping("nearest")
    public ResponseEntity<?> getNearestAttraction(@Valid NearestAttractionRequestDto nearestAttractionRequestDto) {
        return ResponseEntity.ok(attractionService.getNearestAttraction(MemberDistCommandDto.from(nearestAttractionRequestDto)));
    }
    @GetMapping("popular")
    public ResponseEntity<?> getPopularAttraction(@Valid PopularAttractionRequestDto popularAttractionRequestDto) {
        return ResponseEntity.ok(attractionService.getPopularAttraction(MemberDistCommandDto.from(popularAttractionRequestDto)));
    }
}
