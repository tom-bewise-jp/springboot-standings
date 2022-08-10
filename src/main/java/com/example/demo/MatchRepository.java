/**
 * 
 */
package com.example.demo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author bewise
 *
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
	public List<Match> findBySection(int section);
	public List<Match> findByDateBetween(LocalDate from, LocalDate to);
	public List<Match> findByHomeOrAway(String home, String away);
}
