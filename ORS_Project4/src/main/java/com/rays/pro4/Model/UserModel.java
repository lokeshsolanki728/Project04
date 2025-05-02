diff
--- a/ORS_Project4/src/main/java/com/rays/pro4/Model/UserModel.java
+++ b/ORS_Project4/src/main/java/com/rays/pro4/Model/UserModel.java
@@ -64,7 +64,7 @@
 			pstmt.setString(2, bean.getFirstName());
 			pstmt.setString(3, bean.getLastName());
 			pstmt.setString(4, bean.getLogin());
-			pstmt.setString(5, bean.getPassword());
+			pstmt.setString(5, DataUtility.getHashedPassword(bean.getPassword()));
 			// date of birth caste by sql date
 			pstmt.setDate(6, new Date(bean.getDob().getTime()));
 			pstmt.setString(7, bean.getMobileNo());
@@ -214,7 +214,7 @@
 		    pstmt.setString(1, bean.getFirstName());
 		    pstmt.setString(2, bean.getLastName());
 			pstmt.setString(3, bean.getLogin());
-			pstmt.setString(4, bean.getPassword());
+			pstmt.setString(4, DataUtility.getHashedPassword(bean.getPassword()));
 			pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
 			pstmt.setString(6, bean.getMobileNo());
 			pstmt.setLong(7, bean.getRoleId());