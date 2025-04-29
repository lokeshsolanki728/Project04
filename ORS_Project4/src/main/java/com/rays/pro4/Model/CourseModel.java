private Integer nextPK() throws DatabaseException {
    log.debug("Model nextPK Started");
    Connection conn = null;
    int pk = 0;
    try {
        conn = JDBCDataSource.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'ORS_Project4' AND TABLE_NAME = 'ST_COURSE'");
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            pk = rs.getInt(1);
        }
        rs.close();
        pstmt.close();
    } catch (Exception e) {
        log.error("Database Exception while getting next PK for ST_COURSE", e);
        throw new DatabaseException("Exception: Unable to get next primary key for ST_COURSE: " + e.getMessage());
    } finally {
        JDBCDataSource.closeConnection(conn);
    }
    log.debug("Model nextPK End");
    return pk;
}