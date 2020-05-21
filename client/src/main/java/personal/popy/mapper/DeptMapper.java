package personal.popy.mapper;

import personal.popy.entity.Dept;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptMapper {
    Dept select(int id);
    int insert(Dept dept);
     String toString();
}
