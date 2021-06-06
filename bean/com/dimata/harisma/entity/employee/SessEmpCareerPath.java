/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.employee;

import java.util.Date;

/**
 *
 * @author IanRizky
 */
public class SessEmpCareerPath {
	private String company = "";
	private String division = "";
	private String section = "";
	private String position = "";
	private String level = "";
	private String category = "";
	private Date historyFrom;
	private Date historyTo;

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the division
	 */
	public String getDivision() {
		return division;
	}

	/**
	 * @param division the division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @param section the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the historyFrom
	 */
	public Date getHistoryFrom() {
		return historyFrom;
	}

	/**
	 * @param historyFrom the historyFrom to set
	 */
	public void setHistoryFrom(Date historyFrom) {
		this.historyFrom = historyFrom;
	}

	/**
	 * @return the historyTo
	 */
	public Date getHistoryTo() {
		return historyTo;
	}

	/**
	 * @param historyTo the historyTo to set
	 */
	public void setHistoryTo(Date historyTo) {
		this.historyTo = historyTo;
	}
}
