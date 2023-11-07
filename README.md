# 社内システム【取引先情報一覧】
インターンシップ先のチーム開発で、担当したページのコントローラ・サービス・リポジトリ・ビューです。<br>
登録した取引先情報の検索や削除ができる画面です。<br><br>
制作時間:6日<br>

# 使用技術
・Java <br>
・Spring Boot <br>
・Thymeleaf<br>
・MySQL<br>

# こだわったポイント
　コメントを忘れずに書くことや1文づつの改行を統一をするなど、<br>
　他の人が見てもわかりやすくなるように心がけました<br>

# 苦労したポイント
　ページネーションの実装の作業が初めてだったため、<br>
　調べたり実装する作業で苦労しました。<br>

# 機能
・取引先情報の検索機能 <br>
検索項目に一致した取引先情報をデータベースから取得して表示します。<br>

【初期画面】
![](images/torisikiski_toroku_shoki_gamen.PNG)

【無条件検索画面1ページ目】
![](images/torisikiski_toroku_kensaku_mujoken.PNG)

【無条件検索画面2ページ目】
![](images/torisikiski_toroku_kensaku_mujoken2.PNG)

【ID検索】
![](images/torisikiski_toroku_kensaku_id.PNG)

【エラー画面（有効日エラー）】
![](images/torisikiski_toroku_kensaku_error1.PNG)

【エラー画面（検索結果なし）】
![](images/torisikiski_toroku_kensaku_error2.PNG)
<br>
<br>
<br>
・取引先情報の削除機能 <br>
選択した取引先情報を取引先情報リストから削除します。(複数可)<br>

【削除成功時の画面】
![](images/torisikiski_toroku_sakujo_message.PNG)

【削除失敗時の画面（チェックボックス未選択）】
![](images/torisikiski_toroku_sakujo_error.PNG)