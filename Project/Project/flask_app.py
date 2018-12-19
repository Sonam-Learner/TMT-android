import sqlalchemy
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Sequence
from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import sessionmaker

from flask import Flask, session, request, render_template
import json
app = Flask(__name__)
app.secret_key = 'Mydog14'


engine = create_engine('sqlite:///User.sqlite')
Base=declarative_base()
class User(Base):
    __tablename__ = 'users'
    my_pid = Column(Integer, Sequence('user_id_seq'), primary_key=True)
    id = Column(String(50))
    Name = Column(String(50))
    PhoneNumber = Column(Integer)
    BookTitle = Column(String(50))
    Price = Column(Integer)
    Quantity=Column(Integer)
Session = sessionmaker(bind=engine)
Mysession = Session()
    # def __repr__(self):
    #     return "<User(name='%s', fullname='%s', password='%s')>" % (
    #                             self.name, self.fullname, self.password)
Base.metadata.create_all(engine)
@app.route('/')
def index():
	return render_template('index.html')

@app.route('/<b_id>/<b_title>/<b_price>')
def Cart(b_id,b_title,b_price):
	if b_id in session:
		count=session[b_id][0]
		if count<3:
			session[b_id]=[count+1,b_title,b_price]
		else:
			session[b_id]=[count,b_title,b_price]
	else:
		session[b_id]=[1,b_title,b_price]
	print(session)
	count=0
	return render_template('index.html')

@app.route('/checkout/',methods = ['POST'])
def Check():
	return render_template('checkout.html',parent_dict=session)

@app.route('/placeOrder/',methods =['POST'])
def PlaceOrder():
	name=request.form['name']
	Phone=request.form['Mobile']
	for keys,values in session.items():
		item=User(id=keys, Name=name, PhoneNumber=Phone, BookTitle=values[1], Price=values[2], Quantity=values[0])
		Mysession.add(item)
		Mysession.commit()
	return render_template('showOrder.html')

@app.route('/showorder/',methods=['POST'])
def Show_Order():
	name=request.form['name']
	Phone=request.form['Mobile']
	Mydata=[]
	for myvalue in Mysession.query(User).filter(User.Name==name).filter(User.PhoneNumber==Phone):
		newlist=[myvalue.BookTitle, myvalue.Price, myvalue.Quantity]
		Mydata.append(newlist)
	print (Mydata)
	return render_template('showorder1.html',data=Mydata)
