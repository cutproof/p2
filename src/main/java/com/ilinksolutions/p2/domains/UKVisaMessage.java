package com.ilinksolutions.p2.domains;

import java.io.Serializable;

/**
 *
 */
public class UKVisaMessage
{
    private Serializable id;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String email;

    public UKVisaMessage()
    {
    }

    public UKVisaMessage(Serializable id, String firstName, String lastName, String contactNo, String email)
    {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNo = contactNo;
		this.email = email;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
