package com.zwash.booking.pojos;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "media")
public class Media {

	public Media() {

	}
	public Media(MultipartFile logoFile,MultipartFile pictureFile) {

		this.pictureFile=pictureFile;
		this.logoFile=logoFile;
		this.logo=logoFile.getName();
		this.picture=pictureFile.getName();
	}
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public MultipartFile getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(MultipartFile logoFile) {
		this.logoFile = logoFile;
	}

	public MultipartFile getPictureFile() {
		return pictureFile;
	}

	public void setPictureFile(MultipartFile pictureFile) {
		this.pictureFile = pictureFile;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	@PrePersist
	protected void onCreate() {
		setCreatedAt(LocalDateTime.now());
		setUpdatedAt(LocalDateTime.now());
	}

	@PreUpdate
	protected void onUpdate() {
		setUpdatedAt(LocalDateTime.now());
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String logo;

    @Column(nullable = true)
    private String picture;

    @Transient // Exclude from database mapping
    private MultipartFile logoFile;

    @Transient // Exclude from database mapping
    private MultipartFile pictureFile;

	@CreationTimestamp
	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;
}
