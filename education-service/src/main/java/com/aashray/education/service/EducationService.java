package com.aashray.education.service;

import com.aashray.education.dto.*;
import com.aashray.education.entity.AssignmentStatus;
import com.aashray.education.entity.EducationAssignment;
import com.aashray.education.entity.Student;
import com.aashray.education.entity.StudentStatus;
import com.aashray.education.exception.AssignmentNotFoundException;
import com.aashray.education.exception.InvalidAssignmentStateException;
import com.aashray.education.exception.StudentNotFoundException;
import com.aashray.education.exception.UnauthorizedAccessException;
import com.aashray.education.repository.EducationAssignmentRepository;
import com.aashray.education.repository.StudentRepository;
import com.aashray.education.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class EducationService {

    private final StudentRepository studentRepository;
    private final EducationAssignmentRepository assignmentRepository;
    private final EventPublisherService eventPublisherService;

    public EducationService(StudentRepository studentRepository,
                             EducationAssignmentRepository assignmentRepository,
                             EventPublisherService eventPublisherService) {
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.eventPublisherService = eventPublisherService;
    }

    // ---------- NGO: Student registration ----------

    @Transactional
    public StudentResponse registerStudent(UserPrincipal ngo, CreateStudentRequest request) {
        Student student = Student.builder()
                .fullName(request.fullName())
                .age(request.age())
                .gender(request.gender())
                .city(request.city())
                .status(StudentStatus.UNASSIGNED)
                .ngoId(ngo.userId())
                .ngoName(ngo.fullName())
                .build();

        return toStudentResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse updateStudent(UserPrincipal ngo, Long studentId, UpdateStudentRequest request) {
        Student student = findStudentOrThrow(studentId);
        assertStudentOwnedByNgo(student, ngo.userId());

        if (request.fullName() != null) student.setFullName(request.fullName());
        if (request.age() != null) student.setAge(request.age());
        if (request.gender() != null) student.setGender(request.gender());
        if (request.city() != null) student.setCity(request.city());

        return toStudentResponse(studentRepository.save(student));
    }

    public List<StudentResponse> getNgoStudents(UserPrincipal ngo) {
        return studentRepository.findByNgoIdOrderByCreatedAtDesc(ngo.userId()).stream()
                .map(this::toStudentResponse)
                .toList();
    }

    // ---------- NGO: Assign Educator ----------

    @Transactional
    public AssignmentResponse assignEducator(UserPrincipal ngo, AssignEducatorRequest request) {
        Student student = findStudentOrThrow(request.studentId());
        assertStudentOwnedByNgo(student, ngo.userId());

        EducationAssignment assignment = EducationAssignment.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .educatorId(request.educatorId())
                .educatorName(request.educatorName())
                .ngoId(ngo.userId())
                .ngoName(ngo.fullName())
                .subject(request.subject())
                .assignmentDate(request.assignmentDate())
                .status(AssignmentStatus.ACTIVE)
                .build();

        EducationAssignment saved = assignmentRepository.save(assignment);

        student.setStatus(StudentStatus.ASSIGNED);
        studentRepository.save(student);

        eventPublisherService.publishEducatorAssigned(toEvent(saved));
        return toAssignmentResponse(saved);
    }

    @Transactional
    public AssignmentResponse cancelAssignment(UserPrincipal ngo, Long assignmentId) {
        EducationAssignment assignment = findAssignmentOrThrow(assignmentId);
        assertAssignmentOwnedByNgo(assignment, ngo.userId());

        if (assignment.getStatus() != AssignmentStatus.ACTIVE) {
            throw new InvalidAssignmentStateException("Only ACTIVE assignments can be cancelled");
        }

        assignment.setStatus(AssignmentStatus.CANCELLED);
        EducationAssignment saved = assignmentRepository.save(assignment);

        revertStudentToUnassignedIfNoActiveAssignment(assignment.getStudentId());
        return toAssignmentResponse(saved);
    }

    public List<AssignmentResponse> getNgoAssignments(UserPrincipal ngo) {
        return assignmentRepository.findByNgoIdOrderByCreatedAtDesc(ngo.userId()).stream()
                .map(this::toAssignmentResponse)
                .toList();
    }

    // ---------- Educator ----------

    public List<AssignmentResponse> getAssignedStudents(UserPrincipal educator) {
        return assignmentRepository.findByEducatorIdOrderByCreatedAtDesc(educator.userId()).stream()
                .map(this::toAssignmentResponse)
                .toList();
    }

    @Transactional
    public AssignmentResponse completeAssignment(UserPrincipal educator, Long assignmentId) {
        EducationAssignment assignment = findAssignmentOrThrow(assignmentId);
        assertAssignmentOwnedByEducator(assignment, educator.userId());

        if (assignment.getStatus() != AssignmentStatus.ACTIVE) {
            throw new InvalidAssignmentStateException("Only ACTIVE assignments can be marked COMPLETED");
        }

        assignment.setStatus(AssignmentStatus.COMPLETED);
        EducationAssignment saved = assignmentRepository.save(assignment);

        revertStudentToUnassignedIfNoActiveAssignment(assignment.getStudentId());
        return toAssignmentResponse(saved);
    }

    // ---------- Admin ----------

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toStudentResponse)
                .toList();
    }

    public List<AssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toAssignmentResponse)
                .toList();
    }

    public Map<String, Long> getStats() {
        return Map.of(
                "totalStudents", studentRepository.count(),
                "unassignedStudents", studentRepository.countByStatus(StudentStatus.UNASSIGNED),
                "assignedStudents", studentRepository.countByStatus(StudentStatus.ASSIGNED),
                "totalAssignments", assignmentRepository.count(),
                "activeAssignments", assignmentRepository.countByStatus(AssignmentStatus.ACTIVE),
                "completedAssignments", assignmentRepository.countByStatus(AssignmentStatus.COMPLETED)
        );
    }

    public List<AssignmentResponse> getRecentAssignments(int limit) {
        return assignmentRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .map(this::toAssignmentResponse)
                .toList();
    }

    // ---------- helpers ----------

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    private EducationAssignment findAssignmentOrThrow(Long id) {
        return assignmentRepository.findById(id).orElseThrow(() -> new AssignmentNotFoundException(id));
    }

    private void assertStudentOwnedByNgo(Student student, Long ngoId) {
        if (!student.getNgoId().equals(ngoId)) {
            throw new UnauthorizedAccessException("You do not have access to this student");
        }
    }

    private void assertAssignmentOwnedByNgo(EducationAssignment assignment, Long ngoId) {
        if (!assignment.getNgoId().equals(ngoId)) {
            throw new UnauthorizedAccessException("You do not have access to this assignment");
        }
    }

    private void assertAssignmentOwnedByEducator(EducationAssignment assignment, Long educatorId) {
        if (!assignment.getEducatorId().equals(educatorId)) {
            throw new UnauthorizedAccessException("You do not have access to this assignment");
        }
    }

    private void revertStudentToUnassignedIfNoActiveAssignment(Long studentId) {
        boolean hasActive = assignmentRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .anyMatch(a -> a.getStatus() == AssignmentStatus.ACTIVE);

        if (!hasActive) {
            studentRepository.findById(studentId).ifPresent(student -> {
                student.setStatus(StudentStatus.UNASSIGNED);
                studentRepository.save(student);
            });
        }
    }

    private EducatorAssignedEvent toEvent(EducationAssignment a) {
        return EducatorAssignedEvent.builder()
                .assignmentId(a.getId())
                .studentId(a.getStudentId())
                .studentName(a.getStudentName())
                .educatorId(a.getEducatorId())
                .educatorName(a.getEducatorName())
                .ngoId(a.getNgoId())
                .ngoName(a.getNgoName())
                .subject(a.getSubject())
                .build();
    }

    private StudentResponse toStudentResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .fullName(s.getFullName())
                .age(s.getAge())
                .gender(s.getGender())
                .city(s.getCity())
                .status(s.getStatus())
                .ngoId(s.getNgoId())
                .ngoName(s.getNgoName())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    private AssignmentResponse toAssignmentResponse(EducationAssignment a) {
        return AssignmentResponse.builder()
                .id(a.getId())
                .studentId(a.getStudentId())
                .studentName(a.getStudentName())
                .educatorId(a.getEducatorId())
                .educatorName(a.getEducatorName())
                .ngoId(a.getNgoId())
                .ngoName(a.getNgoName())
                .subject(a.getSubject())
                .assignmentDate(a.getAssignmentDate())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
