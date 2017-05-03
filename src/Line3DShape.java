import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 * Created by manatea on 2016/11/17.
 */
public class Line3DShape {
    public Line3DShape(){
        //构建空间 和物体

        // 创建一个虚拟空间
        SimpleUniverse universe = new  SimpleUniverse();
        // 创建一个用来包含对象的数据结构
        BranchGroup group = new BranchGroup();
        // 创建直线形状对象把它加入到group中
        Shape3D shape=new LineShape();
        group.addChild(shape);

        // 安放观察点
        universe.getViewingPlatform().setNominalViewingTransform();
        // 把group加入到虚拟空间中
        universe.addBranchGraph(group);
    }


    public static void main(String[] args) {
        new Line3DShape();
    }
}
