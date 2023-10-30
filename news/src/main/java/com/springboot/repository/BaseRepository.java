package com.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.BaseEntity;

public interface BaseRepository <T extends BaseEntity> extends JpaRepository<T, Long> {

}
