require 'sinatra'
require 'sinatra/json'
require 'json'


class Node
	
	@@nodes = 0

	def initialize(owner, name, lat, long)
		@owner = owner
		@name = name
		@lat = lat
		@long = long
		@@nodes = @@nodes + 1
		@id = @@nodes
	end

	def out
		{:id => @id, :owner => @owner, :name => @name, :lat => @lat, :long => @long}
	end

end

class User
	
	attr_reader :openid
	
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

end

owner = "Jon"

nodes= [Node.new(owner,'Thames Barrier Park', 51.500189, 0.039983), 
		Node.new(owner,'Connaught Bridge',51.506787,0.039983), 
		Node.new(owner, 'Silvertown', 51.502834, 0.038996), 
		Node.new(owner, 'The Crystal', 51.507241,0.016294), 
		Node.new(owner,'Royal Victoria Gardens',51.499708, 0.067062)]
		
users= [User.new('fb990d3a9c8100889a2fb5b04567ec1f0ba086ce5e58da4abb513c12b30ed6ea','Jon L',99999999,9999999),
		User.new('id1243','Will H',1,1),
		User.new('id1276','Curtis K',1,1),
		User.new('id1293','Peter E',1,1)]

get '/nodes' do
  json nodes.map { |n| n.out}
end

post '/nodes' do
	request.body.rewind
	request_payload = JSON.parse request.body.read
	
	newNode = Node.new(request_payload["owner"], request_payload["name"], request_payload["lat"], request_payload["long"])
	nodes = nodes.push(newNode)
	"OK"

end

get '/users' do
	json users.map { |n| n.out }
end

get '/users/:openid' do
	json users.find { |n| n.openid == params[:openid] }.out
end

