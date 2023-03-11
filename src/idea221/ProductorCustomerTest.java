package idea221;

/**
 * 线程通信的应用：生产者/消费者问题
 *
 * 生产者（Productor）将产品交给店员（Clerk），而消费者（Customer）从店员处取走产品，店员一次只能持有固定数量的产品（如20），
 * 如果生产者试图生产更多的产品，店员会叫停生产者，直到有空位了再继续生产；如果没有产品了，店员则会叫停消费者，直到生产出了新的产品。
 *
 */
public class ProductorCustomerTest {
    public static void main(String[] args) {
        Product p=new Product();
        Productor p1=new Productor(p);
        Customer c1=new Customer(p);
        Thread t1=new Thread(p1);
        Thread t2=new Thread(c1);
        t1.setName("生产者");
        t2.setName("消费者");
        t1.start();
        t2.start();
    }
}
class Product{
    private static int product=0;
    //private static Product instance=null;

    //private Product(){}
    public static int getProduct() {
        return product;
    }

    public static void setProduct(int product) {
        Product.product = product;
    }
    /*public static Product getInstance(){
        if (instance==null){
            synchronized (Product.class){
                if (instance==null){
                    instance=new Product();
                }
            }
        }
        return instance;
    }*/
}
class Productor implements Runnable{
    private Product product;
    public Productor(Product product){
        this.product=product;
    }
    int count=0;
    //private Product product=Product.getInstance();
    @Override
    public void run() {
        while (count<100) {
            synchronized (product) {
                if (Product.getProduct() < 20) {
                    if (Product.getProduct() == 0) {
                        product.notify();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Product.setProduct(Product.getProduct() + 1);
                    System.out.println(Thread.currentThread().getName() + "生产完成，现在有" + Product.getProduct() + "个产品");
                } else {
                    try {
                        product.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                count++;
            }
        }
    }
}
class Customer implements Runnable{
    //private Product product=Product.getInstance();
    private Product product;
    public Customer(Product product){
        this.product=product;
    }
    int count=0;

    @Override
    public void run() {
        while (count<100) {
            synchronized (product) {
                if (Product.getProduct() > 0) {
                    if (Product.getProduct() < 20) {
                        product.notify();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Product.setProduct(Product.getProduct() - 1);
                    System.out.println(Thread.currentThread().getName() + "消费成功，现在有" + Product.getProduct() + "个产品");
                } else {
                    try {
                        product.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
