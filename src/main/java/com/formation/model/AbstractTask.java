package com.formation.model;

import java.time.LocalDate;

public abstract class AbstractTask {
	
	/**
	 * le principe POO de l'encapsulation est brisé ici, car les champs de la classe sont publics. 
	 * Cela signifie que n'importe quelle classe peut accéder et modifier directement ces champs, 
	 * ce qui peut entraîner des problèmes d'intégrité des données et de maintenance du code.
	 * Pour respecter le principe d'encapsulation, il est recommandé de rendre les champs privés et de fournir 
	 * des méthodes d'accès (getters) et de modification (setters) pour contrôler l'accès à ces champs (tu l'a déjà fait).
	 *  Cela permet de protéger les données et de garantir que les modifications sont effectuées de manière contrôlée.
	 */
    protected TaskId id;
    protected String title;
    protected boolean done;
    protected LocalDate createAt;

    
    public AbstractTask() {
        this.done = false;
        this.createAt = LocalDate.now();
	}

	/**
     * Ce constructeur ne sera doit être visible que dans les classes fille. il faut utiliser la portabilitée protected au lieux de public.
     */
    public AbstractTask(TaskId id, String title) {
        this.id =id;
        this.title = title;
        this.done = false;
        this.createAt = LocalDate.now();
    }
 
    /**
	 * il faut aussi définir un constructeur qui prend en paramètre les champs de la
	 * classe, afin de pouvoir créer des tâches avec des valeurs spécifiques. public
	 * AbstractTask(TaskId id, String title) {
	 * this.id = id; 
	 * this.title = title; 
	 */


    public TaskId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    
    
    public void setTitle(String title) {
		this.title = title;
	}

	/**
     * il faut aussi définir un setter pour le champ title, sinon il ne sera pas possible 
     * de modifier le titre d'une tâche après sa création.
     * @return
     */

    public LocalDate getCreateAt() {
        return createAt;
    }
    
    public void setId(TaskId id) {
		this.id = id;
	}

    
	public boolean isDone() {
		return done;
	}

	public void markAsDone() {
        this.done = true;
    }

    @Override
    public String toString() {
        return super.toString() + "[id=" + id + ", hashCode=" + super.hashCode() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
		}
        /**
         * On compare en fonction de leur id, car c'est un identifiant unique pour chaque tâche.
         * ou alors on peut comparer en fonction de tous les champs choisis pour définir l'égalité entre deux objets.
         * il faut remplacer 
         * 
         * 		return false;
         * par
         * 		AbstractTask that = (AbstractTask) o;
		 *		return Objects.equals(id, that.id);
         */

		return false;
    }
    
    public abstract String getSummary();

}
