package com.formation.model;

public class SimpleTask extends AbstractTask {
	// problème d'encapsulation. utilise private au lieu du public
    public Priority priority;

    
    
    public SimpleTask() {
    	super(); // super() appel de constructeur vide de la class parent
	}

	/**
     * Si tu utilise ce constructeur, tu auras
     * des task avec un title null.
     */
    public SimpleTask(Priority priority) {
    	super();
        this.priority = priority;
    }

    public SimpleTask(TaskId id, String title, Priority priority) {
    	/**
    	 * ce n'est pas un bonne pratique pour initialiser les champs du parent
    	 * à la place crée un constructeur dans le parent qui prend en paramètre les deux
    	 * champs que tu souhaite initialiser ici (id et title) pour 
    	 * remplace
    	 *  this.id = id;
         *	this.title = title;
         *par 
         *  super(id,title);
    	 */
        super(id,title);
        this.priority = priority;
    }

    public String getSummary() {
        return "[SIMPLE]" + super.title + "(" + priority + ")";
    }
}
