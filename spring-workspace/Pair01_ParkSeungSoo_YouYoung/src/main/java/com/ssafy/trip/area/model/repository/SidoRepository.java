package com.ssafy.trip.area.model.repository;

import com.ssafy.trip.area.model.entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SidoRepository extends JpaRepository<Sido, Integer> {
}
