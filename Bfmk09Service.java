package com.seizou.kojo.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seizou.kojo.domain.dto.Bfmk09ShowDto;
import com.seizou.kojo.domain.form.Bfmk09Form;
import com.seizou.kojo.domain.repository.Bfmk09Repository;

/**
 * 取引先情報一覧 Service
 * @author T.Kiku
 */
@Service
public class Bfmk09Service {

	@Autowired
	Bfmk09Repository repository;

	/**
	 * 検索項目の値が一致している情報を取得
	 * @param form
	 * @return
	 */
	public List<Bfmk09ShowDto> search(Bfmk09Form form) {
		
		//取引先終了年月日が取引開始年月日より前の場合
		if (form.getPromiseStrtDate() != null && form.getPromiseEndDate() != null) {
			if (form.getPromiseStrtDate().isAfter(form.getPromiseEndDate())) {
				//nullを返却
				return null;
			}
		}

		//検索結果を返却
		return repository.search(form);
	}

	/**
	 * 選択した情報を削除する
	 * @param deleteIdList	削除用IDリスト
	 */
	public void deleteAllById(List<String> deleteIdList) {
		//削除処理を実行
		repository.deleteAllById(deleteIdList);
	}

	/**
	 * 取引区分名取得
	 * @param div_cd	区分値
	 * @return			区分名称
	 */
	public String getDivision(String divCd) {
		//取引区分名を返却
		return repository.getDivision(divCd);
	}
}
