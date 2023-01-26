package visual;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class VisualDAO implements VisualService {
	@Autowired @Qualifier("hr") private SqlSession sql;

	@Override
	public List<HashMap<String, Object>> department() {
		return sql.selectList("visual.department");
	}

	@Override
	public List<HashMap<String, Object>> hirement_year() {
		return sql.selectList("visual.hirement_year");
	}

	@Override
	public List<HashMap<String, Object>> hirement_month() {
		return sql.selectList("visual.hirement_month");
	}

	@Override
	public List<HashMap<String, Object>> hirement_top3_year() {
		return sql.selectList("visual.hirement_top3_year");
	}

	@Override
	public List<HashMap<String, Object>> hirement_top3_month() {
		return sql.selectList("visual.hirement_top3_month");
	}

	@Override
	public List<HashMap<String, Object>> hirement_top3_year(HashMap<String, Object> map) {
		int begin = Integer.parseInt( map.get("begin").toString() );
		int end = Integer.parseInt( map.get("end").toString() );
		StringBuffer range = new StringBuffer();
		for(int year=begin; year<=end; year++) {
			 //'2001' y2001, '2002' y2002
			if( year>begin ) range.append(", ");
			range.append("'").append(year).append("' y").append(year);
		}
		map.put("range", range.toString());
		return sql.selectList("visual.hirement_top3_year", map);
	}

}
