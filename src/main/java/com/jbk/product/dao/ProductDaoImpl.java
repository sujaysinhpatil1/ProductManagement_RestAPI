package com.jbk.product.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbk.product.entity.Product;

@Component
public class ProductDaoImpl implements ProductDao {

	// @Autowired
	// private SessionFactory sessionFactory;
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean saveProduct(Product product) {
		Session session = null;
		Transaction transaction = null;
		Product prd = null;
		boolean isSaved = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			prd = session.get(Product.class, product.getProductId());
			if (prd == null) {
				session.save(product);
				transaction.commit();
				isSaved = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return isSaved;
	}

	@Override
	public List<Product> getAllProduct() {
		Session session = null;
		List<Product> list = null;
		try {
			session = sessionFactory.openSession();
			list = session.createCriteria(Product.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return list;
	}

	@Override
	public Product getProductById(String productId) {
		Session session = sessionFactory.openSession();
		Product product = null;
		try {
			product = session.get(Product.class, productId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (product != null) {
				session.close();
			}
		}

		return product;
	}

	@Override
	public boolean deleteProduct(String productId) {
		Session session = null;
		Transaction transaction = null;
		boolean isDeleted = false;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Product product = session.get(Product.class, productId);
			if (product != null) {
				session.delete(product);
				transaction.commit();
				isDeleted = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return isDeleted;
	}

	@Override
	public boolean updateProduct(Product product) {
		Session session = null;
		// Session session2 = null;
		Transaction transaction = null;
		Product prd = null;
		boolean isUpdated = false;

		try {
			session = sessionFactory.openSession();
			// session2 = sessionFactory.openSession(); two sessions are not required
			transaction = session.beginTransaction();
			prd = session.get(Product.class, product.getProductId());
			if (prd != null) {
				session.evict(prd);
				session.update(product);
				transaction.commit();
				isUpdated = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return isUpdated;
	}

	@Override
	public List<Product> sortProduct(String sortBy) {

		return null;
	}

	@Override
	public double getTotalCount() {

		return 0;
	}

	@Override
	public Product getMaxPriceProduct() {

		return null;
	}

	@Override
	public double countSumProductPrice() {

		return 0;
	}

	@Override
	public int uploadProductList(List<Product> list) {
		Session session = null;
		Transaction transaction = null;
		Product prd = null;
		int count = 0;
		try {

			for (Product product : list) {
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				session.save(product);
				transaction.commit();
				count++;

				if (session != null) {
					session.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return count;
	}

}
