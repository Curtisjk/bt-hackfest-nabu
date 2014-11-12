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

owner = "Jon"

nodes= [Node.new(owner,'Thames Barrier Park', '51.500189N', '0.039983E'), 
		Node.new(owner,'Connaught Bridge','51.506787N','0.039983E'), 
		Node.new(owner, 'Silvertown', '51.502834N', '0.038996E'), 
		Node.new(owner, 'The Crystal', '51.507241N','0.016294'), 
		Node.new(owner,'Royal Victoria Gardens','51.499708N', '0.067062E')]

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

