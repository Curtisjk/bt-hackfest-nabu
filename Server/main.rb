require 'sinatra'
require 'sinatra/json'
require 'json'


class Node
	
	@@nodes = 0

	def initialize(owner, name, lat, long, level, faction)
		@owner = owner
		@name = name
		@lat = lat
		@long = long
		@level = level
		@faction = faction
		@@nodes = @@nodes + 1
		@id = @@nodes
	end

	def out
		{:id => @id, :owner => @owner, :name => @name, :lat => @lat, :long => @long, :level => @level, :faction => @faction}
	end

end

class User
	
	attr_reader :openid
	attr_reader :level

	
	@@users = 0

	def initialize(openid, name, xp, level)
		@openid = openid
		@name = name
		@xp = xp
		@level = level
		@@users = @@users + 1
		@id = @@users
	end

	def out
		{:id => @id, :openid => @openid, :name => @name, :xp => @xp, :level => @level}
	end
	
	def increaseXp(amount)
		@xp = @xp + amount
		@level = (@xp / 100) + 1
	end

end

owner = "Jon"

nodes= [Node.new(owner,'Thames Barrier Park', 51.500189, 0.039983,1,1), 
		Node.new(owner,'Connaught Bridge',51.506787,0.039983,2,2), 
		Node.new(owner, 'Silvertown', 51.502834, 0.038996,2,2), 
		Node.new(owner, 'The Crystal', 51.507241,0.016294,4,1), 
		Node.new(owner,'Royal Victoria Gardens',51.499708, 0.067062,3,2)]
		
users= [User.new('fb990d3a9c8100889a2fb5b04567ec1f0ba086ce5e58da4abb513c12b30ed6ea','Jon L',195,2),
		User.new('92490e15daa840a531b74050850e9bbe9639ddb879c52499fb807eedd84ae07e','Will H',105,2),
		User.new('id1276','Curtis K',90,1),
		User.new('id1293','Peter E',178,2)]

get '/nodes' do
  json nodes.map { |n| n.out}
end

post '/nodes' do
	request.body.rewind
	request_payload = JSON.parse request.body.read
	
	user = users.find { |n| n.openid == request_payload["owner"] }
	nodeLevel = user.level;
	user.increaseXp(20)

	
	localIds = request_payload["locals"]
	
	localIds.each do |id|
	
		user = users.find { |n| n.openid == id }
		if user == nil
			nodeLevel = nodeLevel + 1
		else
			nodeLevel = nodeLevel + user.level
			user.increaseXp(5)
		end
		
	end
		
	newNode = Node.new(request_payload["owner"], request_payload["name"], request_payload["lat"], request_payload["long"], nodeLevel,1)
	nodes = nodes.push(newNode)

	
	json newNode.out

end

get '/users' do
	json users.map { |n| n.out }
end

post '/userlist' do

	request.body.rewind
	request_payload = JSON.parse request.body.read
	
	requsers = request_payload["users"]
	
	foundUsers = requsers.map do |n|
	
		user = users.find { |m| m.openid == n }
		
		if user == nil
			user = User.new(n,'Unknown',0,1)
		end
		
		user
	end
	
	json foundUsers.map { |n| n.out }
end

get '/users/:openid' do
	json users.find { |n| n.openid == params[:openid] }.out
end

