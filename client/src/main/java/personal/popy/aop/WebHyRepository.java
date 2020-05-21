package personal.popy.aop;

import org.springframework.stereotype.Repository;

@Repository
public interface WebHyRepository extends HyRepository<Object> {
	void print(String argv);
}
