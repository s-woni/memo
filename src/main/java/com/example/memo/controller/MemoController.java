package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping
    // public MemoResponseDto createMemo(@RequestBody MemoRequestDto dto) {
    public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto) {

        // 식별자가 1씩 증가
        // Collections.max -> 최대값 꺼내기
        // memoList.keySet() -> 메모 리스트 안에 있는 키 값 꺼내기
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;

        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());

        memoList.put(memoId, memo);

        // return new MemoResponseDto(memo);
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    // public MemoResponseDto findMemoById(@PathVariable Long id) {
    public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id) {

        Memo memo = memoList.get(id);

        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // return new MemoResponseDto(memo);
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @GetMapping
    // public List<MemoResponseDto> findAllMemos() {
    public ResponseEntity<List<MemoResponseDto>> findAllMemos() {

        List<MemoResponseDto> responseList = new ArrayList<>();

        for (Memo memo : memoList.values()) {
            MemoResponseDto responseDto = new MemoResponseDto(memo);
            responseList.add(responseDto);
        }

        // responseList = memoList.values().stream().map(MemoResponseDto::new).toList();

        // return responseList;
        return new ResponseEntity<>(new ArrayList<>(responseList), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    // public MemoResponseDto updateMemoById(
    public ResponseEntity<MemoResponseDto> updateMemoById(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto) {

        Memo memo = memoList.get(id);

        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (dto.getTitle() == null || dto.getContents() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        memo.update(dto);

        // return new MemoResponseDto(memo);
        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateTitle(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto) {

        Memo memo = memoList.get(id);

        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (dto.getTitle() == null || dto.getContents() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        memo.updateTitle(dto);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    // public void deleteMemo(@PathVariable Long id) {
    public ResponseEntity<Void> deleteMemo(@PathVariable Long id) {

        if (memoList.containsKey(id)) {
            memoList.remove(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
