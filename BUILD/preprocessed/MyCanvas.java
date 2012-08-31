import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;

public class MyCanvas extends Canvas {

 private Graphics3D graphics3d;
 private Camera camera;
 private Light light;
 private float angle = 0.0f;
 private Transform transform = new Transform();
 private Background background = new Background();
 private VertexBuffer vbuffer;
 private IndexBuffer indexbuffer;
 private Appearance appearance;
 private Material material = new Material();
 private Image image;
 private float d_angle;

 public MyCanvas() {
  // Устанавливаем Displayable для прослушивания команд от пользователя
  setCommandListener(new CommandListener() {
   public void commandAction(Command c, Displayable d) {
     if (c.getCommandType() == Command.EXIT) {
       MIDletMain.quitApp();
     } else if (c.getCommandType() == Command.OK) {
       d_angle*=-1;
     }
   }
  });
  try { init();}
  catch(Exception e) { e.printStackTrace();}
 }

 /**
  * инициализация
  */
 private void init() throws Exception {
  setFullScreenMode(true);
  addCommand(new Command("Exit", Command.EXIT, 1));
  addCommand(new Command("Test", Command.OK, 1));
  graphics3d = Graphics3D.getInstance();
  d_angle=1.0f;
  camera = new Camera();
  camera.setPerspective( 60.0f,
   (float)getWidth()/ (float)getHeight(),
   1.0f,
   1000.0f );

  light = new Light();
  light.setColor(0xffffff);
  light.setIntensity(1.25f);

  short[] vert = {
   5, 5, 5, -5, 5, 5, 5,-5, 5, -5,-5, 5,
   -5, 5,-5, 5, 5,-5, -5,-5,-5, 5,-5,-5,
   -5, 5, 5, -5, 5,-5, -5,-5, 5, -5,-5,-5,
   5, 5,-5, 5, 5, 5, 5,-5,-5, 5,-5, 5,
   5, 5,-5, -5, 5,-5, 5, 5, 5, -5, 5, 5,
   5,-5, 5, -5,-5, 5, 5,-5,-5, -5,-5,-5 };

  VertexArray vertArray = new VertexArray(vert.length / 3, 3, 2);
  vertArray.set(0, vert.length/3, vert);

  // Задаем нормали куба
  byte[] norm = {
   0, 0, 127, 0, 0, 127, 0, 0, 127, 0, 0, 127,
   0, 0,-127, 0, 0,-127, 0, 0,-127, 0, 0,-127,
   -127, 0, 0, -127, 0, 0, -127, 0, 0, -127, 0, 0,
   127, 0, 0, 127, 0, 0, 127, 0, 0, 127, 0, 0,
   0, 127, 0, 0, 127, 0, 0, 127, 0, 0, 127, 0,
   0,-127, 0, 0,-127, 0, 0,-127, 0, 0,-127, 0 };

  VertexArray normArray = new VertexArray(norm.length / 3, 3, 1);
  normArray.set(0, norm.length/3, norm);

  // Задаем текстурные координаты
  short[] tex = {
   1, 0, 0, 0, 1, 1, 0, 1,
   1, 0, 0, 0, 1, 1, 0, 1,
   1, 0, 0, 0, 1, 1, 0, 1,
   1, 0, 0, 0, 1, 1, 0, 1,
   1, 0, 0, 0, 1, 1, 0, 1,
   1, 0, 0, 0, 1, 1, 0, 1 };

  VertexArray texArray = new VertexArray(tex.length / 2, 2, 2);
  texArray.set(0, tex.length/2, tex);

  int[] stripLen = { 4, 4, 4, 4, 4, 4 };

  // VertexBuffer для нашего объекта
  VertexBuffer vb = vbuffer = new VertexBuffer();
  vb.setPositions(vertArray, 1.0f, null);
  vb.setNormals(normArray);
  vb.setTexCoords(0, texArray, 1.0f, null);

  indexbuffer = new TriangleStripArray( 0, stripLen );

  // изображение для текстуры
  image = Image.createImage( "/nine.png" );
  Image2D image2D = new Image2D( Image2D.RGB, image );
  Texture2D texture = new Texture2D( image2D );
  texture.setFiltering(Texture2D.FILTER_NEAREST,
        Texture2D.FILTER_NEAREST);
  texture.setWrapping(Texture2D.WRAP_CLAMP,
       Texture2D.WRAP_CLAMP);
  texture.setBlending(Texture2D.FUNC_MODULATE);

  // создаем вид
  appearance = new Appearance();
  appearance.setTexture(0, texture);
  appearance.setMaterial(material);
  material.setColor(Material.DIFFUSE, 0xFFFFFFFF);
  material.setColor(Material.SPECULAR, 0xFFFFFFFF);
  material.setShininess(100.0f);

  background.setColor(0xffffcc);
 }

 protected void paint(Graphics g) {
  graphics3d.bindTarget(g, true,
      Graphics3D.DITHER |
      Graphics3D.TRUE_COLOR);
  graphics3d.clear(background);

  // устанавливаем камеру
  Transform transform = new Transform();
  transform.postTranslate(0.0f, 0.0f, 30.0f);
  graphics3d.setCamera(camera, transform);

  // Устанавливаем имточники света
  graphics3d.resetLights();
  graphics3d.addLight(light, transform);

  // Задаем вращение
  angle += d_angle;
  transform.setIdentity();
  transform.postRotate(angle, 1.0f, 1.0f, 1.0f);

  graphics3d.render(vbuffer, indexbuffer, appearance, transform);
  graphics3d.releaseTarget();
 }
}