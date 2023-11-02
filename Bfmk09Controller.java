package com.seizou.kojo.domain.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seizou.kojo.domain.dto.Bfmk09ShowDto;
import com.seizou.kojo.domain.form.Bfmk09Form;
import com.seizou.kojo.domain.service.Bfmk09Service;

/**
 * 取引先情報一覧 Controller
 * @author T.Kiku
 */
@Controller
@RequestMapping("/b-forme_Kojo")
public class Bfmk09Controller {

	/** サービス */
	@Autowired
	Bfmk09Service service;

	/** メッセージソース */
	@Autowired
	private MessageSource messageSource;

	/**
	 * 初期画面表示
	 * @param 取引先情報フォーム
	 * @return 画面名
	 */
	@GetMapping("/pc/209")
	public String initView(Bfmk09Form form) {
		return "bfmk09View";
	}

	/**
	 * 検索結果を表示（検索ボタン）
	 * @param form			入力フォーム
	 * @param model			モデル
	 * @param targetPage	現在ページ
	 * @return	画面名
	 */
	@PostMapping("/pc/209")
	public String search(Bfmk09Form form, Model model, Integer targetPage) {

		//リスト表示の上限数を設定
		Integer pageMax = 6;

		//現在ページがない場合、0（1ページ）を設定
		if (targetPage == null) {
			targetPage = 0;
		}

		//表示リストを取得
		List<Bfmk09ShowDto> searchList = service.search(form);
		
		//表示リストがnullの場合
		if(searchList == null) {
			//エラーメッセージを設定
			model.addAttribute(
					"error", messageSource.getMessage(
							"msclner03", null, Locale.getDefault()));
			
			return "bfmk09View";
		}

		//表示する行数のクラスを初期化
		PagedListHolder<Bfmk09ShowDto> pagenation 
			= new PagedListHolder<Bfmk09ShowDto>(searchList);

		//現在ページを設定
		pagenation.setPage(targetPage);

		//１ページの表示数を設定
		pagenation.setPageSize(pageMax); 

		//リスト表示する件数を絞り込む
		searchList = pagenation.getPageList();

		//リスト表示する件数分、ループ
		for (Bfmk09ShowDto bfmk09ShowDto : searchList) {
			//区分名称を取得
			String customerDiv = service.getDivision(bfmk09ShowDto.getCustomerDiv());
			//取引先区分を区分名称に修正
			bfmk09ShowDto.setCustomerDiv(customerDiv);
		}

		//検索結果が0の場合
		if (searchList.isEmpty()) {
			//エラーメッセージを設定
			model.addAttribute(
					"error", messageSource.getMessage(
							"msclner01", null, Locale.getDefault()));
		//検索結果が1件以上の場合
		}else {
			model.addAttribute("pages", pagenation);		//表示するページ
			model.addAttribute("searchList", searchList);	//取引先情報リスト
		}

		return "bfmk09View";
	}

	/**
	 * メニュー画面へ戻る（戻るボタン）
	 * @return 画面名
	 */
	@PostMapping(value = "/pc/209", params = "back")
	public String backBtn() {
		return "bfkt02View";
	}

	/**
	 * 画面の初期化（クリアボタン）
	 * @param form	入力フォーム
	 * @param model	モデル
	 * @return		画面名
	 */
	@PostMapping(value = "/pc/209", params = "clear")
	public String clearBtn(Bfmk09Form form, Model model) {
		form = new Bfmk09Form();
		model.addAttribute("bfmk09Form", form);

		return "bfmk09View";
	}

	/**
	 * 削除処理（削除ボタン）
	 * @param deleteIdList		取引先IDリスト
	 * @param form			入力フォーム
	 * @param model			モデル
	 * @return				画面名
	 */
	@PostMapping(value = "/pc/209", params = "delete")
	public String delete(@RequestParam(name = "delete_id", required = false) List<String> deleteIdList,
			Bfmk09Form form, Model model) {

		//チェックボックスのチェック数が0の場合
		if (deleteIdList == null || deleteIdList.isEmpty()) {
			//エラーメッセージを表示
			model.addAttribute("error",
					messageSource.getMessage(
							"msclner02", null, Locale.getDefault()));
		} else {
			//削除処理の実行
			service.deleteAllById(deleteIdList);
			
			//メッセージを表示
			model.addAttribute("message",
					messageSource.getMessage(
							"msclnrs01", null, Locale.getDefault()));
		}
		
		return search(form, model, null);
	}

	/**
	 * 次のページに遷移する
	 * @param form 入力フォーム
	 * @param nextPage 次のページのインデックス
	 * @param model モデル
	 * @return 画面名
	 */
	@PostMapping(value = "/pc/209", params = "next_page")
	public String nextPage(Bfmk09Form form, @RequestParam(name = "nextPage") Integer nextPage, Model model) {
		return search(form, model, nextPage);
	}

	/**
	 * 前のページに遷移する
	 * @param form 入力フォーム
	 * @param nextPage 次のページのインデックス
	 * @param model モデル
	 * @return 画面名
	 */
	@PostMapping(value = "/pc/209", params = "prev_page")
	public String prevPage(Bfmk09Form form, @RequestParam(name = "prevPage") Integer prevPage, Model model) {
		return search(form, model, prevPage);
	}

	/**
	 * 最初のページに遷移する
	 * @param form 入力フォーム
	 * @param nextPage 次のページのインデックス
	 * @param model モデル
	 * @return 画面名
	 */
	@PostMapping(value = "/pc/209", params = "start_page")
	public String startPage(Bfmk09Form form, @RequestParam(name = "startPage") Integer startPage, Model model) {
		return search(form, model, startPage);
	}

	/**
	 * 最後のページに遷移する
	 * @param form 入力フォーム
	 * @param nextPage 次のページのインデックス
	 * @param model モデル
	 * @return 画面名
	 */
	@PostMapping(value = "/pc/209", params = "end_page")
	public String endPage(Bfmk09Form form, @RequestParam(name = "endPage") Integer endPage, Model model) {
		return search(form, model, endPage);
	}
}
