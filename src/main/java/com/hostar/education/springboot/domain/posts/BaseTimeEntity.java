package com.hostar.education.springboot.domain.posts;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 모든 Entity의 상위 클래스가 되어서 Entity들의 CreateData, ModifiedDate를 자동으로 관리하는 역할을 한다.
 */
@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들도 칼럼으로 인식하도록함
@EntityListeners(AuditingEntityListener.class) // 이 클래스에 Auditing 기능포함
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createDate; // 생성일

    @LastModifiedDate
    private  LocalDateTime modifiedDate; // 수정일
}
