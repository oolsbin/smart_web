package visual;

import java.util.HashMap;
import java.util.List;

public interface VisualService {
	List<HashMap<String, Object>> department(); //부서별 사원수
	List<HashMap<String, Object>> hirement_year(); //년도도별 채용인원수
	List<HashMap<String, Object>> hirement_month();//월별 채용인원수
	List<HashMap<String, Object>> hirement_top3_year(); //인원수 상위3위 부서에 대한 년도도별 채용인원수
	List<HashMap<String, Object>> hirement_top3_year(HashMap<String, Object> map); //인원수 상위3위 부서에 대한 년도도별 채용인원수
	List<HashMap<String, Object>> hirement_top3_month();//인원수 상위3위 부서에 대한 월별 채용인원수
}
