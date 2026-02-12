<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.sql.*, javax.naming.*, javax.sql.DataSource" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Correction Base de Données</title>
            <style>
                body {
                    font-family: sans-serif;
                    padding: 20px;
                    line-height: 1.6;
                }

                .success {
                    color: green;
                }

                .error {
                    color: red;
                }
            </style>
        </head>

        <body>
            <h1>Outil de Correction des Dates MySQL</h1>
            <% Connection conn=null; try { Context initContext=new InitialContext(); DataSource ds=(DataSource)
                initContext.lookup("java:/gestion_rh_jeeDS"); conn=ds.getConnection(); out.println("<p>Connexion à la
                base de données réussie.</p>");

                // Corriger les dates à '0000-00-00 00:00:00' en les passant à NULL ou à la date actuelle
                String[] tables = {"notifications", "absences"};
                String[] dateCols = {"date_envoi", "date_lecture", "date_absence"};

                int totalFixed = 0;

                Statement stmt = conn.createStatement();

                // Pour les notifications
                int n1 = stmt.executeUpdate("UPDATE notifications SET date_envoi = NOW() WHERE date_envoi IS NULL OR
                CAST(date_envoi AS CHAR) = '0000-00-00 00:00:00'");
                int n2 = stmt.executeUpdate("UPDATE notifications SET date_lecture = NULL WHERE CAST(date_lecture AS
                CHAR) = '0000-00-00 00:00:00'");

                // Pour les absences
                int a1 = stmt.executeUpdate("UPDATE absences SET date_absence = NOW() WHERE date_absence IS NULL OR
                CAST(date_absence AS CHAR) = '0000-00-00 00:00:00'");

                out.println("<p class='success'>Mise à jour terminée :</p>");
                out.println("<ul>");
                    out.println("<li>Notifications (date_envoi) corrigées : " + n1 + "</li>");
                    out.println("<li>Notifications (date_lecture) corrigées : " + n2 + "</li>");
                    out.println("<li>Absences corrigées : " + a1 + "</li>");
                    out.println("</ul>");

                } catch (Exception e) {
                out.println("<p class='error'>Erreur lors de la correction : " + e.getMessage() + "</p>");
                e.printStackTrace(new java.io.PrintWriter(out));
                } finally {
                if (conn != null) try { conn.close(); } catch (SQLException e) {}
                }
                %>
                <p><a href="index.html">Retour à l'accueil</a></p>
        </body>

        </html>