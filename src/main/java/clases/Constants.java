package clases;

public class Constants {
    //##################### paths layouts ################################
    public static final String WINDOW_FRAME_PATH = "/layout/window_layout.fxml";
    public static final String ALTERNATIVE_MAINTENANCE_PATH ="/layout/alternative_maintenance_layout.fxml";
    //##################### estados del mantenimiento ################################
    public static final String ESTADO_UNIDAD_ACTIVO = "Activo";
    public static final String ESTADO_UNIDAD_INACTIVO = "Inactivo";
    public static final String UNIDAD_SIN_MANTENIMIENTO = "Nunca ha recibido mantenimiento";
    public static final String UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO = "No hay fechas programadas";
    public static final String UNIDAD_EN_MANTENIMIENTO = "En mantenimiento";
    public static final String ESTADO_MANTENIMIENTO_OK = "Terminado";
    public static final String ESTADO_MANTENIMIENTO_STOP = "No se termino";
    public static final String ESTADO_MANTENIMIENTO_PENDIENTE = "Pendiente";

    //###################### window style #################################
    public static final String WINDOW_CONTROLLER_CONTAINER_STYLE = "-fx-background-color: rgba(0, 0, 0, 0.3); -fx-border-radius: 10px; -fx-spacing: 0px;";
    private static final String WINDOW_CONTROLLER_BUTTON_STYLE = "-fx-max-width: 27px; -fx-max-height: 27px;";
    public static final String MAX_MIN_BUTTON_STYLE = WINDOW_CONTROLLER_BUTTON_STYLE + "-fx-background-color: transparent;";
    public static final String MAX_MIN_BUTTON_HOVER_STYLE = WINDOW_CONTROLLER_BUTTON_STYLE + "-fx-background-color: rgba(200,200,200,0.4);";
    private static final String SHRINK_SHAPE = "-fx-shape: 'M558.56,638.34a15.24,15.24,0,0,0-5.08-11.64,8.2,8.2,0,0,0-3.74-3,5,5,0,0,0-3-1.39h0a9,9,0,0,0-5.28-1.14l-1.53,0h-62.1c-2.09-.08-4.2-.52-6.2.52h0a.37.37,0,0,0-.54.12A8.37,8.37,0,0,0,465.8,624a13.29,13.29,0,0,0-6.78,8,15.33,15.33,0,0,0,0,14h0a13.37,13.37,0,0,0,6.8,8,8.31,8.31,0,0,0,5.25,2.22l.25.18.3-.06h0c2,1,4.1.61,6.19.52h16.46l2.77.26c0,.17,0,.34,0,.51a9.77,9.77,0,0,0-3,3,15.18,15.18,0,0,0-3.75,3.75,10,10,0,0,0-3,3,15.35,15.35,0,0,0-3.75,3.75,9.09,9.09,0,0,0-2.84,2.85,16.33,16.33,0,0,0-3.89,3.89,14.49,14.49,0,0,0-3.6,3.6h0a10.92,10.92,0,0,0-3.13,3.15h0a15,15,0,0,0-3.74,3.75,21.33,21.33,0,0,0-4.5,4.5h0l-1.35,1.34h0a23.28,23.28,0,0,0-4.64,4.66A9,9,0,0,0,453,701.7a16.6,16.6,0,0,0-3.9,3.9L447,707.69h0l-2.24,2.24-3.16,3.16h0c-.69.7-1.38,1.4-2.08,2.09h0a16.05,16.05,0,0,0-3.89,3.91,14.57,14.57,0,0,0-3.76,3.75h0l-2.07,2.09h0a16.64,16.64,0,0,0-3.9,3.91l-1.35,1.34-2.24,2.24h0a16.6,16.6,0,0,0-3.89,3.91h0a15.05,15.05,0,0,0-3.75,3.76,10.06,10.06,0,0,0-3,3,9.87,9.87,0,0,0-3,3,20.72,20.72,0,0,0-4.31,4.31c-9,7.87-9.39,20-.84,27.41a7.68,7.68,0,0,0,4.54,3h0a6.84,6.84,0,0,0,4.63,1.39,4.72,4.72,0,0,0,3,.14l3-.21a6,6,0,0,0,3.77-1.26,4.36,4.36,0,0,0,2.93-1.62,2.25,2.25,0,0,0,1.6-1.24h0a8.94,8.94,0,0,0,3-2.91l3-3h0l3.73-3.78,3-3,3-3h0L445,760h0l3-3,2.24-2.28h0l3-3h0l3.74-3.73h0l3.74-3.75,2.25-2.24h0l4.49-4.5,3.74-3.75,2.25-2.25h0l4.48-4.5h0l3-3h0l3.74-3.77h0l3.74-3.73,3-3h0l3.74-3.76,3-3h0l3.73-3.75h0l3.75-3.75,3-3,3.94-4h0l2.79-2.79,3.74-3.75h0l3-3h0c1.16,1,.59,2.34.6,3.51.07,6,0,12,0,18a15.33,15.33,0,0,0,2.4,8.86,9.43,9.43,0,0,0,3.62,4.52,7.56,7.56,0,0,0,3.6,2.57h0a6.62,6.62,0,0,0,3.76,1.44h0a5.69,5.69,0,0,0,3.82.53,5,5,0,0,0,3-.09,6,6,0,0,0,3.74-.83,5.33,5.33,0,0,0,3-1.44,7.33,7.33,0,0,0,3.73-2.94A15.4,15.4,0,0,0,558.56,705Q558.69,671.67,558.56,638.34Z M555.06,608a15.23,15.23,0,0,0,5.07,11.64,8.26,8.26,0,0,0,3.74,3,4.92,4.92,0,0,0,3,1.38h0a9,9,0,0,0,5.28,1.15l1.53,0h62.1c2.09.08,4.21.51,6.2-.52h0a.37.37,0,0,0,.54-.12,8.48,8.48,0,0,0,5.27-2.21,13.29,13.29,0,0,0,6.78-8,15.33,15.33,0,0,0,0-14h0a13.38,13.38,0,0,0-6.79-8,8.36,8.36,0,0,0-5.26-2.22l-.25-.18-.3.06h0c-2-1-4.1-.61-6.19-.53H619.34l-2.77-.25c0-.17,0-.34,0-.51a9.77,9.77,0,0,0,3-3,15.05,15.05,0,0,0,3.75-3.76,9.8,9.8,0,0,0,3-3,15.35,15.35,0,0,0,3.75-3.75,9.09,9.09,0,0,0,2.84-2.85,16.33,16.33,0,0,0,3.89-3.89,14.36,14.36,0,0,0,3.6-3.61h0a10.77,10.77,0,0,0,3.13-3.14h0a15,15,0,0,0,3.74-3.75,21.33,21.33,0,0,0,4.5-4.5h0l1.35-1.34h0a23.55,23.55,0,0,0,4.64-4.66,9.06,9.06,0,0,0,2.84-2.84,16.41,16.41,0,0,0,3.9-3.9c.69-.7,1.39-1.4,2.09-2.09h0l2.24-2.24,3.16-3.16h0l2.08-2.1h0a15.84,15.84,0,0,0,3.89-3.9,14.57,14.57,0,0,0,3.76-3.75h0l2.08-2.09h0a16.6,16.6,0,0,0,3.89-3.91l1.35-1.34,2.24-2.24h0a16.83,16.83,0,0,0,3.9-3.91h0a15.18,15.18,0,0,0,3.74-3.76,846.42,846.42,0,0,1,6-6,21.26,21.26,0,0,0,4.32-4.31c9-7.87,9.38-20,.83-27.41a7.59,7.59,0,0,0-4.54-2.95h0a6.84,6.84,0,0,0-4.63-1.39,4.72,4.72,0,0,0-3-.14l-3,.2a6.1,6.1,0,0,0-3.77,1.27,4.36,4.36,0,0,0-2.93,1.62,2.25,2.25,0,0,0-1.6,1.24h0a9,9,0,0,0-3,2.9l-3,3h0l-3.73,3.77-3,3-3,3h0l-2.25,2.25h0l-3,3-2.24,2.27h0l-3,3h0l-3.74,3.73h0L652.9,502l-2.25,2.24h0l-4.48,4.5-3.75,3.75-2.25,2.25h0l-4.48,4.5h0l-3,3h0L629,526h0l-3.73,3.73-3,3h0l-3.74,3.75-3,3h0l-3.73,3.74h0L608,547l-3,3-3.94,4h0l-2.79,2.79-3.74,3.75h0l-3,3h0c-1.15-1-.59-2.34-.6-3.51-.07-6,0-12,0-18a15.36,15.36,0,0,0-2.4-8.87,9.52,9.52,0,0,0-3.62-4.52,7.6,7.6,0,0,0-3.6-2.56h0a6.55,6.55,0,0,0-3.76-1.44h0a5.69,5.69,0,0,0-3.82-.53,5.12,5.12,0,0,0-3,.08A6,6,0,0,0,567,525a5.34,5.34,0,0,0-3,1.45,7.33,7.33,0,0,0-3.73,2.94,15.39,15.39,0,0,0-5.19,11.94Q554.92,574.68,555.06,608Z';";
    public static final String SHRINK_SHAPE_STYLE = "-fx-background-color: white;" + SHRINK_SHAPE;
    public static final String SHRINK_SHAPE_HOVER_STYLE = SHRINK_SHAPE_STYLE;
    private static final String MAXIMIZE_SHAPE = "-fx-shape: 'M24 9h-2v-5h-7v-2h9v7zm-9 13v-2h7v-5h2v7h-9zm-15-7h2v5h7v2h-9v-7zm9-13v2h-7v5h-2v-7h9z';";
    public static final String MAXIMIZE_SHAPE_STYLE = "-fx-background-color: white;" + MAXIMIZE_SHAPE;
    public static final String MAXIMIZE_SHAPE_HOVER_STYLE = MAXIMIZE_SHAPE_STYLE;
    private static final String CLOSE_SHAPE = "-fx-shape: 'm12 10.93 5.719-5.72c.146-.146.339-.219.531-.219.404 0 .75.324.75.749 0 .193-.073.385-.219.532l-5.72 5.719 5.719 5.719c.147.147.22.339.22.531 0 .427-.349.75-.75.75-.192 0-.385-.073-.531-.219l-5.719-5.719-5.719 5.719c-.146.146-.339.219-.531.219-.401 0-.75-.323-.75-.75 0-.192.073-.384.22-.531l5.719-5.719-5.72-5.719c-.146-.147-.219-.339-.219-.532 0-.425.346-.749.75-.749.192 0 .385.073.531.219z';";
    public static final String CLOSE_SHAPE_STYLE = "-fx-background-color: white;" + CLOSE_SHAPE;
    public static final String CLOSE_SHAPE_HOVER_STYLE = "-fx-background-color: white;" + CLOSE_SHAPE;
    public static final String CLOSE_BUTTON_STYLE = WINDOW_CONTROLLER_BUTTON_STYLE + "-fx-background-color: transparent;";
    public static final String CLOSE_BUTTON_HOVER_STYLE = WINDOW_CONTROLLER_BUTTON_STYLE + "-fx-background-color: #D72638;";

}
