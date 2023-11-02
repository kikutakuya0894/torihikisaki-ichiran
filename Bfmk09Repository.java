package com.seizou.kojo.domain.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.seizou.kojo.domain.dto.Bfmk09ShowDto;
import com.seizou.kojo.domain.form.Bfmk09Form;

/**
 * 取引先情報一覧 Repository
 * @author T.Kiku
 */
@Repository
@Transactional
public class Bfmk09Repository {

	@Autowired
	JdbcTemplate jdbc;

	/**
	 * 検索項目の値が一致している情報を取得
	 * @param form　	入力フォーム
	 * @return			取引先情報リスト
	 */
	public List<Bfmk09ShowDto> search(Bfmk09Form form) throws DataAccessException {

		//検索フラグ
		boolean searchFlg = false;

		//取引先情報リスト
		List<Bfmk09ShowDto> bfmk09ShowDtoList = new ArrayList<Bfmk09ShowDto>();

		//sql文
		String query = "SELECT"
				+ " customer_id"
				+ ",customer_name"
				+ ",customer_div"
				+ ",promise_strt_date"
				+ ",promise_end_date"
				+ " FROM client_info";

		query += " WHERE";
		query += " del_flg = 0";

		//検索ワードリスト
		List<Object> searchWordList = new ArrayList<Object>();

		//取引先IDがnullの場合、空文字を設定
		if (form.getCustomerId() == null) {
			form.setCustomerId("");
		}

		//取引先名称がnullの場合、空文字を設定
		if (form.getCustomerName() == null) {
			form.setCustomerName("");
		}

		//フォームの項目がすべてnullではないとき
		if (!form.getCustomerId().isBlank()
				|| !form.getCustomerName().isBlank()
				|| form.getCustomerDiv() != null
				|| form.getPromiseEndDate() != null
				|| form.getPromiseStrtDate() != null) {

			searchFlg = true;

			//取引先IDが空文字以外の場合 
			if (!form.getCustomerId().isBlank()) {
				query += " AND"
						+ " customer_id like ?";

				//検索ワードリストにフォームの値を追加
				searchWordList.add("%" + form.getCustomerId() + "%");
			}

			//取引先区分がnull,以外の場合 
			if (form.getCustomerDiv() != null) {
				int index = 0;
				for (Object div : form.getCustomerDiv()) {
					switch (index) {
					case 0:
						query += " AND";
						break;

					default:
						query += " OR";
						break;
					}
					query += " customer_div = ?";

					//検索ワードリストにフォームの値を追加
					searchWordList.add(div);
					index++;
				}
			}

			//取引先名称が空文字以外の場合 
			if (!form.getCustomerName().isBlank()) {
				query += " AND"
						+ " customer_name = ?";

				//検索ワードリストにフォームの値を追加
				searchWordList.add(form.getCustomerName());
			}

			//取引開始年月日がnull,空文字以外の場合 
			if (form.getPromiseStrtDate() != null) {
				query += " AND"
						+ " promise_strt_date >= ?";

				//検索ワードリストにフォームの値を追加
				searchWordList.add(form.getPromiseStrtDate().toString());
			}

			//取引終了年月日がnull,空文字以外の場合 
			if (form.getPromiseEndDate() != null) {
				query += " AND"
						+ " promise_end_date <= ?";

				// 検索ワードリストにフォームの値を追加
				searchWordList.add(form.getPromiseEndDate().toString());
			}

		}

		query += " ORDER BY customer_id ASC"
				+ " LIMIT 500";

		//情報をDBから取得する 
		List<Map<String, Object>> getList = new ArrayList<Map<String, Object>>();

		//検索フラグがtrueの場合
		if (searchFlg) {
			//検索項目がある場合
			getList = jdbc.queryForList(query, searchWordList.toArray());
		} else {
			//全件検索
			getList = jdbc.queryForList(query);
		}

		//取得した情報をリストにして返却する
		for (Map<String, Object> map : getList) {
			Bfmk09ShowDto bfmk09ShowDto = new Bfmk09ShowDto();
			bfmk09ShowDto.setCustomerId((String) map.get("customer_id"));
			bfmk09ShowDto.setCustomerName((String) map.get("customer_name"));
			bfmk09ShowDto.setCustomerDiv((String) map.get("customer_div"));
			bfmk09ShowDto.setPromiseStrtDate(LocalDate.parse(map.get("promise_strt_date").toString()));
			bfmk09ShowDto.setPromiseEndDate(LocalDate.parse(map.get("promise_end_date").toString()));

			bfmk09ShowDtoList.add(bfmk09ShowDto);
		}

		return bfmk09ShowDtoList;
	}

	/**
	 * 配列に入っているIDを全て削除
	 * @param deleteIdList	削除IDリスト
	 */
	public void deleteAllById(List<String> deleteIdList) throws DataAccessException {
		//削除IDリストの分、ループ
		for (String id : deleteIdList) {
			//sql文
			String query = "UPDATE client_info SET del_flg = 1 WHERE customer_id = ?";

			//削除処理を実行
			jdbc.update(query, id);
		}
	}

	/**
	 * 区分名称を取得
	 * @param div_cd	区分値
	 * @return			区分名称
	 */
	public String getDivision(String divCd) throws DataAccessException {

		//sql文
		String query = "SELECT"
				+ " div_name"
				+ " FROM division"
				+ " WHERE"
				+ " big_func_cd = 'KKTS'"
				+ " AND"
				+ " middle_func_cd = '00'"
				+ " AND"
				+ " div_cd = ?"
				+ " LIMIT 1";

		//区分名称を取得
		Map<String, Object> division = jdbc.queryForMap(query, divCd);

		return (String) division.get("div_name");
	}

}
