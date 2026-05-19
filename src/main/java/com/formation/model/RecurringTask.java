package com.formation.model;

import java.time.LocalDate;

public class RecurringTask extends AbstractTask {
	
	//problème d'encapsulation. utilise private au lieu du public
    public int intervalDays;

    public String getSummary() {
        return "[RECURRENT]" + super.title + "(tous les " + intervalDays + " jours)";
    }

    public LocalDate getNextOccurrence() {
        return createAt.plusDays(intervalDays);
    }
}
