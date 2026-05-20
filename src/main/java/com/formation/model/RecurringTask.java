package com.formation.model;

import java.time.LocalDate;

public class RecurringTask extends AbstractTask {
	
	//problème d'encapsulation. utilise private au lieu du public
    private int intervalDays;

    
    
    public RecurringTask() {
		super();
		this.intervalDays = 0;
	}

	public RecurringTask(TaskId id, String title, int intervalDays) {
		super(id, title);
		this.intervalDays = intervalDays;
	}

	public int getIntervalDays() {
		return intervalDays;
	}

	public void setIntervalDays(int intervalDays) {
		this.intervalDays = intervalDays;
	}

	public String getSummary() {
        return "[RECURRENT]" + super.title + "(tous les " + intervalDays + " jours)";
    }

    public LocalDate getNextOccurrence() {
        return createAt.plusDays(intervalDays);
    }
}
