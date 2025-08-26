package com.italohreis.medly.repositories.specs;

import com.italohreis.medly.enums.Specialty;
import com.italohreis.medly.models.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpec {


    public static Specification<Doctor> hasName(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Doctor> hasCrm(String crm) {
        return (root, query, cb) ->
                cb.like(root.get("crm"), "%" + crm + "%");
    }

    public static Specification<Doctor> hasSpecialty(Specialty specialty) {
        return (root, query, cb) ->
                cb.equal(root.get("specialty"), specialty);
    }
}
