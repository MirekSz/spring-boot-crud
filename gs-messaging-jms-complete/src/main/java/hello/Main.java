package hello;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.atomikos.datasource.ResourceException;
import com.atomikos.datasource.ResourceTransaction;
import com.atomikos.datasource.xa.XAResourceTransaction;
import com.atomikos.datasource.xa.XATransactionalResource;
import com.atomikos.icatch.CompositeTransaction;
import com.atomikos.icatch.CompositeTransactionManager;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.icatch.system.Configuration;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jms.AtomikosConnectionFactoryBean;

public class Main {

	public static void main(String[] args) throws Exception {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setUniqueResourceName("postgres");
		ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
		Properties p = new Properties();
		p.setProperty("user", "test_jenkins");
		p.setProperty("password", "test");
		p.setProperty("serverName", "strumyk-next-db");
		p.setProperty("portNumber", "5432");
		p.setProperty("databaseName", "test");
		ds.setXaProperties(p);
		ds.setMaxPoolSize(5);

		AtomikosDataSourceBean ds2 = new AtomikosDataSourceBean();
		ds2.setUniqueResourceName("postgres2");
		ds2.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
		Properties p2 = new Properties();
		p2.setProperty("user", "verto_dev");
		p2.setProperty("password", "verto_devverto_dev");
		p2.setProperty("serverName", "strumyk-next-db");
		p2.setProperty("portNumber", "5432");
		p2.setProperty("databaseName", "verto_dev");
		ds2.setXaProperties(p2);
		ds2.setMaxPoolSize(5);

		ActiveMQXAConnectionFactory activeMQXAConnectionFactory = new org.apache.activemq.ActiveMQXAConnectionFactory();
		activeMQXAConnectionFactory.setBrokerURL("tcp://localhost:61616");
		AtomikosConnectionFactoryBean atomikosConnectionFactoryBean = new com.atomikos.jms.AtomikosConnectionFactoryBean();
		atomikosConnectionFactoryBean.setXaConnectionFactory(activeMQXAConnectionFactory);
		atomikosConnectionFactoryBean.setUniqueResourceName("jms1");

		JmsTemplate jmsTemplate = new org.springframework.jms.core.JmsTemplate();
		jmsTemplate.setSessionTransacted(true);
		jmsTemplate.setConnectionFactory(atomikosConnectionFactoryBean);

		UserTransactionManager userTransactionManager = new com.atomikos.icatch.jta.UserTransactionManager();
		userTransactionManager.setForceShutdown(false);
		userTransactionManager.setTransactionTimeout(10);

		UserTransactionImp userTransactionImp = new com.atomikos.icatch.jta.UserTransactionImp();
		userTransactionImp.setTransactionTimeout(10);

		JtaTransactionManager jtaTransactionManager = new org.springframework.transaction.jta.JtaTransactionManager();
		jtaTransactionManager.setUserTransaction(userTransactionImp);
		jtaTransactionManager.setTransactionManager(userTransactionManager);

		TransactionStatus transaction = jtaTransactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			XAResource xAResource = new XAResource() {

				@Override
				public void commit(Xid xid, boolean onePhase) throws XAException {
					// TODO Auto-generated method stub
					System.out.println(xid);

				}

				@Override
				public void end(Xid xid, int flags) throws XAException {
					// TODO Auto-generated method stub
					System.out.println(xid);

				}

				@Override
				public void forget(Xid xid) throws XAException {
					// TODO Auto-generated method stub
					System.out.println(xid);

				}

				@Override
				public int getTransactionTimeout() throws XAException {
					// TODO Auto-generated method stub
					return 10000;
				}

				@Override
				public boolean isSameRM(XAResource xares) throws XAException {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public int prepare(Xid xid) throws XAException {
					if (2 > 1) {
						throw new XAException(XAException.XA_RBROLLBACK) {
							@Override
							public String getMessage() {
								// TODO Auto-generated method stub
								return "fuck off";
							}
						};
					}
					System.out.println(xid);
					return XAException.XA_RBROLLBACK;
				}

				@Override
				public Xid[] recover(int flag) throws XAException {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void rollback(Xid xid) throws XAException {
					// TODO Auto-generated method stub
					System.out.println(xid);

				}

				@Override
				public boolean setTransactionTimeout(int seconds) throws XAException {
					// TODO Auto-generated method stub
					return true;
				}

				@Override
				public void start(Xid xid, int flags) throws XAException {
					// TODO Auto-generated method stub
					System.out.println("s");
				}
			};

			XATransactionalResource xaTransactionalResource = new XATransactionalResource("miro") {
				@Override
				public synchronized XAResource getXAResource() {
					return xAResource;
				}

				@Override
				public ResourceTransaction getResourceTransaction(CompositeTransaction ct)
						throws ResourceException, IllegalStateException {
					// TODO Auto-generated method stub
					return super.getResourceTransaction(ct);
				}

				@Override
				protected XAResource refreshXAConnection() throws ResourceException {
					return xAResource;
				}

			};
			Connection connection = ds.getConnection();
			Statement createStatement = connection.createStatement();
			createStatement.execute("UPDATE OPERATOR SET VERSION=VERSION+1	 WHERE ID_OPERATOR=10000");
			Connection connection2 = ds2.getConnection();
			Statement createStatement2 = connection2.createStatement();
			createStatement2.execute("UPDATE OPERATOR SET VERSION=VERSION+1 WHERE ID_OPERATOR=100000");

			jmsTemplate.convertAndSend("top", "mirek");

			Configuration.addResource(xaTransactionalResource);
			CompositeTransactionManager ret = Configuration.getCompositeTransactionManager();
			XAResourceTransaction resourceTransaction = (XAResourceTransaction) xaTransactionalResource
					.getResourceTransaction(ret.getCompositeTransaction());
			resourceTransaction.setXAResource(xaTransactionalResource.getXAResource());
			ret.getCompositeTransaction().addParticipant(resourceTransaction);

			jtaTransactionManager.commit(transaction);
		} catch (Exception e) {
			e.printStackTrace();
			jtaTransactionManager.rollback(transaction);
		}
		System.out.println("KOniec");
	}

}
