package com.marcelo.scrumBoard.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="users")
public class User {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String email;    
    private String accountType;
    @Size(min = 8, max = 200)
    @JsonIgnore
    private String password;
    @Transient
    @JsonIgnore
    private String passwordConfirmation;
    
    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Project> projects;
   
    @OneToMany(mappedBy="user", fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Task> tasks;
    
    @Column(updatable=false)
    private Date createdAt;
    private Date updatedAt;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    		name = "projects_members",
    		joinColumns = @JoinColumn(name = "user_id"),
    		inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnore
    private List<Project> allProjects;
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    		name = "projects_clients",
    		joinColumns = @JoinColumn(name = "user_id"),
    		inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnore
    private List<Project> allClientsProj;
    
    
    public User() {
    	
    }
    @PrePersist
    protected void onCreate(){
	    this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
	    this.updatedAt = new Date();
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<Project> getAllProjects() {
		return allProjects;
	}
	public void setAllProjects(List<Project> allProjects) {
		this.allProjects = allProjects;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public List<Project> getAllClientsProj() {
		return allClientsProj;
	}
	public void setAllClientsProj(List<Project> allClientsProj) {
		this.allClientsProj = allClientsProj;
	}
    
    
    
}
