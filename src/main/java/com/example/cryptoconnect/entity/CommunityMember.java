package com.example.cryptoconnect.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"communityId", "userId"}))
public class CommunityMember {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private Long communityId;
	    private Long userId;

	    private String role;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getCommunityId() {
			return communityId;
		}

		public void setCommunityId(Long communityId) {
			this.communityId = communityId;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		} 
}
