package com.stelliocode.backend.repository;
import com.stelliocode.backend.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT COALESCE(SUM(p.price), 0) FROM Project p WHERE FUNCTION('DATE', p.createdAt) >= :startDate")
    int getTotalRevenueByMonth(LocalDate startDate);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = 'COMPLETED' AND FUNCTION('DATE', p.updatedAt) >= :startDate")
    int getCompletedProjectsByMonth(LocalDate startDate);

    @Query("SELECT COUNT(p) FROM Project p WHERE FUNCTION('DATE', p.createdAt) >= :startDate")

    int getNewProjectsByMonth(LocalDate startDate);
    @Query("SELECT p FROM Project p JOIN DeveloperProject dp ON p.id = dp.project.id WHERE dp.developer.id = :developerId")
    Page<Project> findByDeveloperId(@Param("developerId") UUID developerId, Pageable pageable);

    @Query("SELECT p FROM Project p JOIN DeveloperProject dp ON p.id = dp.project.id WHERE dp.developer.id = :developerId AND p.status = :status")
    Page<Project> findByDeveloperIdAndStatus(@Param("developerId") UUID developerId, @Param("status") String status, Pageable pageable);

    @Query("""
    SELECT FUNCTION('TO_CHAR', p.createdAt, 'YYYY-MM'),
           SUM(CASE WHEN p.status = 'IN_PROGRESS' THEN 1 ELSE 0 END),
           SUM(CASE WHEN p.status = 'COMPLETED' THEN 1 ELSE 0 END)
    FROM Project p
    WHERE p.createdAt >= :startDate
    GROUP BY FUNCTION('TO_CHAR', p.createdAt, 'YYYY-MM')
    ORDER BY FUNCTION('TO_CHAR', p.createdAt, 'YYYY-MM')
""")
    List<Object[]> getProjectsStatsLast6Months(@Param("startDate") LocalDateTime startDate);


    Page<Project> findAll(Pageable pageable);

    @Query("""
    SELECT dp.project.id FROM DeveloperProject dp
    WHERE dp.developer.id = :developerId
    AND dp.project.status = 'IN_PROGRESS'
    ORDER BY dp.assignedAt DESC
""")
    List<UUID> findCurrentProjectIdsByDeveloperId(@Param("developerId") UUID developerId);

    @Query("""
        SELECT COUNT(dp) FROM DeveloperProject dp
        WHERE dp.developer.id = :developerId
    """)
    int countByDeveloperId(@Param("developerId") UUID developerId);

    @Query("SELECT s.id, s.title, COUNT(p.id) AS projectCount " +
            "FROM Service s " +
            "LEFT JOIN Project p ON s.id = p.service.id AND p.createdAt >= :startOfYear " +
            "GROUP BY s.id, s.title")
    List<Object[]> countProjectsByServiceSince(@Param("startOfYear") LocalDateTime startOfYear);
}