package com.tacs.rest.entity;

public class UserRole {

	enum Role {
		  ADMIN,
		  USER
		}
	
	private int id;
	private Role roleType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Role getRoleType() {
		return roleType;
	}
	
	public void setRoleType(Role roleType) {
		this.roleType = roleType;
	}
	
	public void setRole(String string) {
		
		for(Role role : Role.values()) {
			if(role.name().equals(string)) {
				this.roleType = role;
			}
		}
	}
	
	
}
