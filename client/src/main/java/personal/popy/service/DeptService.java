package personal.popy.service;

import personal.popy.entity.Dept;
import personal.popy.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeptService {
    @Autowired
    public DeptMapper mapper;

    public Dept select(int id){
        return mapper.select(id);
    }

    public int insert(Dept dept){
        return mapper.insert(dept);
    }
}
