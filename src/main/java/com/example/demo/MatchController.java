package com.example.demo;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MatchController {
	@Autowired
	MatchRepository matchRepository;
	@Autowired
	TeamRepository teamRepository;

	@GetMapping(value = "/matches")
	public String matches(
			@RequestParam(name = "section", defaultValue = "-1") int section, 
			@RequestParam(name = "month", defaultValue = "-1") int month,
			@RequestParam(name = "team", defaultValue = "") String team,
			Model model) {
		List<Match> list;
		List<Team> targetTeams = teamRepository.findByAbbr(team);
		if (section >= 1 && section <= 34) {
			list = matchRepository.findBySection(section);
		} else if (month >= 1 && month <= 12) {
			list = matchRepository.findByDateBetween(LocalDate.of(2022, month, 1), LocalDate.of(2022, month, 1).with(TemporalAdjusters.lastDayOfMonth()));
		} else if (targetTeams != null && targetTeams.size() > 0) {
			list = matchRepository.findByHomeOrAway(team, team);
		} else {
			list = matchRepository.findAll();
		}
		model.addAttribute("matches", list);
		List<Team> teams = teamRepository.findAll();
		model.addAttribute("teams", teams);
		return "matches";
	}

	@GetMapping(value = "/edit_matches")
	public String editMatches(Model model) {
		List<Match> list = matchRepository.findAll();
		model.addAttribute("matches", list);
		return "edit_matches";
	}

	@GetMapping(value = "/match/new")
	public String newMatch(Model model) {
		List<Team> teams = teamRepository.findAll();

		model.addAttribute("teams", teams);
		model.addAttribute("sections", 34);
		return "new_match";
	}

	@PostMapping(value = "/match/new")
	public String newMatch(@RequestParam(name = "section") Integer section,
			@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(name = "home") String home, @RequestParam(name = "away") String away,
			@RequestParam(name = "goals_for") Integer goalsFor,
			@RequestParam(name = "goals_against") Integer goalsAgainst, Model model) {
		Match match = new Match();
		match.setSection(section);
		match.setDate(date);
		match.setHome(home);
		match.setAway(away);
		match.setGoalsFor(goalsFor);
		match.setGoalsAgainst(goalsAgainst);
		matchRepository.saveAndFlush(match);
		return "redirect:/edit_matches";
	}

	@GetMapping(value = "/match/edit/{id}")
	public String editMatch(@PathVariable(name = "id") Integer id, Model model) {
		Optional<Match> match = matchRepository.findById(id);
		if (match == null || match.isEmpty()) {
			return "redirect:/matches";
		}
		model.addAttribute("match", match.get());
		List<Team> teams = teamRepository.findAll();
		model.addAttribute("teams", teams);
		return "edit_match";
	}

	@PostMapping(value = "/match/edit/{id}")
	public String editMatch(@PathVariable(name = "id") Integer id, @RequestParam(name = "section") Integer section,
			@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(name = "home") String home, @RequestParam(name = "away") String away,
			@RequestParam(name = "goals_for") Integer goalsFor,
			@RequestParam(name = "goals_against") Integer goalsAgainst, Model model) {
		Optional<Match> o = matchRepository.findById(id);
		if (o == null || o.isEmpty()) {
			return "redirect:/matches";
		}
		Match match = o.get();
		match.setSection(section);
		match.setDate(date);
		match.setHome(home);
		match.setAway(away);
		match.setGoalsFor(goalsFor);
		match.setGoalsAgainst(goalsAgainst);
		matchRepository.saveAndFlush(match);
		return "redirect:/edit_matches";
	}
}
