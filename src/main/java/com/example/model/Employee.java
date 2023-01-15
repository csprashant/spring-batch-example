package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "employee")
public class Employee  {
	@Id
	private Integer empno;
	private String ename;
	private Integer salary;
	private  String email;
	private  Integer grossSalary;
	private  Integer  netSalary;
	
	public Integer getEmpno() {
		return empno;
	}
	
	public void setEmpno(Integer empno) {
		this.empno = empno;
	}
	
	public String getEname() {
		return ename;
	}
	
	public void setEname(String ename) {
		this.ename = ename;
	}
	
	public Integer getSalary() {
		return salary;
	}
	
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getGrossSalary() {
		return grossSalary;
	}
	
	public void setGrossSalary(Integer grossSalary) {
		this.grossSalary = grossSalary;
	}
	
	public Integer getNetSalary() {
		return netSalary;
	}
	
	public void setNetSalary(Integer netSalary) {
		this.netSalary = netSalary;
	}

	@Override
	public String toString() {
		return "Employee [empno=" + empno + ", ename=" + ename + ", salary=" + salary + ", email=" + email
				+ ", grossSalary=" + grossSalary + ", netSalary=" + netSalary + "]";
	}
	
}