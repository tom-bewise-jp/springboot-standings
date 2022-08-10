package com.example.demo;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="matches")
public class Match {
	@Setter @Getter
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Setter @Getter
	private int section;
	@Setter @Getter
	private LocalDate date;
	@Setter @Getter
	private String home;
	@Setter @Getter
	private String away;
	@Setter @Getter
	private int goalsFor;
	@Setter @Getter
	private int goalsAgainst;
	
}
